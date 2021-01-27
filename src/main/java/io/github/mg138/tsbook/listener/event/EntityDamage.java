package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.StatusEffect;
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
    final BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();
    static final Map<Player, Long> damageCD = new HashMap<>();
    static final Map<Entity, Long> lastDamageTime = new HashMap<>();

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof LivingEntity)) return;
        if (e instanceof ArmorStand || e instanceof Item) return;
        ((LivingEntity) event.getEntity()).setNoDamageTicks(0);

        if (event.isCancelled()) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        switch (cause) {
            case FIRE_TICK:
            case FIRE:
            case LAVA:
                if (lastDamageTime.putIfAbsent(e, System.currentTimeMillis()) != null) {
                    if ((System.currentTimeMillis() - lastDamageTime.get(e)) > 500) {
                        simpleDamage((LivingEntity) event.getEntity(), 2, StatType.DAMAGE_IGNIS);
                        lastDamageTime.replace(e, System.currentTimeMillis());
                    }
                }
                event.setCancelled(true);
                return;
        }
        if (event.isCancelled()) return;

        if (event instanceof EntityDamageByEntityEvent) {
            if (helper.isMythicMob(e)) {
                ConfigurationSection mob = Book.getCfg().getMMMob(helper.getMythicMobInstance(e).getType().getInternalName());
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
        Supplier<Stream<StatType>> damageTypes = DamageType.DAMAGES::stream;

        stats.getStats().forEach(statMap -> {
            StatType key = statMap.getKey();
            if (damageTypes.get().anyMatch(type -> type.equals(key))) {
                damages.put(key, statMap.getValue().getStat());
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
                        EffectHandler.apply(StatusEffect.BURNING, damaged, power, (int) (currentDamage / 6));
                        break;
                    case DAMAGE_PHYSICAL:
                        power = currentDamage / 12;
                        if (power < 5) break;
                        EffectHandler.apply(StatusEffect.BLEEDING, damaged, power, (int) (currentDamage / 14));
                        break;
                }
            }
            displayDamage(currentDamage, damageType, loc, critical);
        }
        event.setDamage(damage);
    }

    public static boolean simpleDamage(LivingEntity entity, double power, StatType type) {
        if (entity.isDead()) return false;

        double damage = EntityDamage.calculateDamage(power, 0, 1);
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