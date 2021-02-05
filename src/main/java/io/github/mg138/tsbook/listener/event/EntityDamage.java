package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.data.StatusEffect;
import io.github.mg138.tsbook.entities.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.stat.DamageType;
import io.github.mg138.tsbook.items.data.stat.Stat;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.entities.EffectHandler;
import io.github.mg138.tsbook.items.ItemInstance;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.mg138.tsbook.listener.event.utils.DamageIndicator.displayDamage;

public class EntityDamage implements Listener {
    private static final BukkitAPIHelper mythicMobHelper = MythicMobs.inst().getAPIHelper();
    private static final HashMap<Player, Long> damageCD = new HashMap<>();
    private static final HashMap<Entity, Long> lastDamageTime = new HashMap<>();

    public static void unload() {
        damageCD.clear();
        lastDamageTime.clear();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        lastDamageTime.remove(event.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        damageCD.remove(event.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (entity instanceof ArmorStand || entity instanceof Item) return;
        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.setNoDamageTicks(0);

        if (event.isCancelled()) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        switch (cause) {
            case FIRE_TICK:
            case FIRE:
            case LAVA:
                if (lastDamageTime.putIfAbsent(entity, System.currentTimeMillis()) != null) {
                    if ((System.currentTimeMillis() - lastDamageTime.get(entity)) > 500) {
                        simpleDamage(livingEntity, 2, StatType.DAMAGE_IGNIS);
                        lastDamageTime.replace(entity, System.currentTimeMillis());
                    }
                }
                event.setCancelled(true);
                return;
            case FALL:
                if (EffectHandler.hasEffect(livingEntity, StatusEffectType.FALL_DAMAGE_RESISTANCE)) {
                    StatusEffect effect = EffectHandler.getEffect(livingEntity, StatusEffectType.FALL_DAMAGE_RESISTANCE);
                    assert effect != null;
                    simpleDamage(livingEntity, event.getDamage() * (1 - effect.power), StatType.DAMAGE_NONE);
                    event.setCancelled(true);
                    return;
                }
        }
        if (event.isCancelled()) return;

        if (event instanceof EntityDamageByEntityEvent) {
            if (mythicMobHelper.isMythicMob(entity)) {
                ConfigurationSection mob = Book.getCfg().getMMMob(mythicMobHelper.getMythicMobInstance(entity).getType().getInternalName());
                if (mob != null) {
                    HashMap<StatType, Double> map = new HashMap<>();
                    for (String key : Objects.requireNonNull(mob.getConfigurationSection("defense")).getKeys(false)) {
                        map.put(StatType.valueOf(key.toUpperCase()), mob.getDouble("defense." + key));
                    }
                    damagedByEntity((EntityDamageByEntityEvent) event, map);
                    return;
                }
            }
            damagedByEntity((EntityDamageByEntityEvent) event, new HashMap<>());
        }
    }

    public static HashMap<StatType, Double> getItemDamage(ItemStats stats) {
        HashMap<StatType, Double> damages = new HashMap<>();
        ItemIdentification identification = stats.getIdentification();
        Supplier<Stream<StatType>> damageTypes = DamageType.DAMAGES::stream;

        stats.getStats().forEach((type, statMap) -> {
            if (damageTypes.get().anyMatch(damageType -> damageType.equals(type))) {
                damages.put(type, statMap.getValue().getStat() * identification.getStatPercentage(type));
            }
        });
        return damages;
    }

    protected void damagedByEntity(EntityDamageByEntityEvent event, HashMap<StatType, Double> def) {
        Entity damaged = event.getEntity();
        if (!(damaged instanceof LivingEntity)) return;

        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            if (damageCD.putIfAbsent(player, System.currentTimeMillis()) != null) {
                if ((System.currentTimeMillis() - damageCD.get(player)) < 500) {
                    event.setCancelled(true);
                    return;
                }
                damageCD.replace(player, System.currentTimeMillis());
            }

            EntityEquipment equipment = player.getEquipment();
            if (equipment == null) return;

            ItemStack item = equipment.getItemInMainHand();
            if (item.getType() == Material.AIR || item.getItemMeta() == null) return;

            ItemInstance inst = ItemUtils.getInstByItem(Book.getInst(), item, "item");
            if (inst == null) return;

            ItemStats stats = inst.getStats();
            if (stats == null) {
                simpleDamage((LivingEntity) damaged, 1, StatType.DAMAGE_NONE);
                return;
            }

            Stat rawCritChance = stats.getStat(StatType.CRITICAL);
            double critChance = rawCritChance == null ? 0 : rawCritChance.getStat();

            complexDamage(event, critChance, getItemDamage(stats), def);
            return;
        }

        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            PersistentDataContainer container = arrow.getPersistentDataContainer();

            HashMap<StatType, Double> damages = new HashMap<>();
            for (NamespacedKey key : container.getKeys()) {
                if (key.getNamespace().equals("tsbooklib")) {
                    StatType type = StatType.valueOf(key.getKey().toUpperCase());
                    damages.put(type, container.get(key, PersistentDataType.DOUBLE));
                }
            }

            complexDamage(event, container.getOrDefault(new NamespacedKey(Book.getInst(), "CRITICAL"), PersistentDataType.DOUBLE, 0.0), damages, def);
            return;
        }

        displayDamage(event.getFinalDamage(), StatType.DAMAGE_NONE, event.getEntity().getLocation(), false);
    }

    public static void complexDamage(EntityDamageByEntityEvent event, double critChance, HashMap<StatType, Double> damages, HashMap<StatType, Double> def) {
        double damage = 0.0;
        LivingEntity damaged = (LivingEntity) event.getEntity();
        Location loc = damaged.getLocation();

        for (StatType damageType : damages.keySet()) {
            boolean critical = (new Random().nextFloat() < ((critChance + 10) / 100));
            double currentDamage = calculateDamage(
                    damages.get(damageType),
                    def.getOrDefault(damageType, 0.0),
                    1 + (critical ? 1 : 0)
            );
            damage += currentDamage;
            if (new Random().nextFloat() < 0.45) {
                double power;
                switch (damageType) {
                    case DAMAGE_IGNIS:
                        power = currentDamage / 8;
                        if (power < 5) break;
                        EffectHandler.apply(StatusEffectType.BURNING, damaged, power, (int) (currentDamage / 6));
                        break;
                    case DAMAGE_PHYSICAL:
                        power = currentDamage / 12;
                        if (power < 5) break;
                        EffectHandler.apply(StatusEffectType.BLEEDING, damaged, power, (int) (currentDamage / 14));
                        break;
                }
            }
            displayDamage(currentDamage, damageType, loc, critical);
        }
        event.setDamage(damage);
    }

    public static boolean simpleDamage(LivingEntity entity, double damage, StatType type) {
        if (entity.isDead()) return false;

        entity.damage(damage);
        entity.setNoDamageTicks(0);
        displayDamage(damage, type, entity.getLocation(), false);
        return true;
    }

    public static double calculateDamage(double damage, double defense, double modifier) {
        damage *= modifier;

        if (defense > 0) {
            damage *= (1 - (defense / (defense + 100)));
        } else if (defense < 0) {
            defense *= -1;
            damage *= (1 + (defense / (defense + 100)));
        }

        return damage;
    }
}