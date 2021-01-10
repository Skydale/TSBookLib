package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.StatRange;
import io.github.mg138.tsbook.items.data.StatType;
import io.github.mg138.tsbook.entities.EntityEffect;
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

import static io.github.mg138.tsbook.listener.event.utils.DamageIndicator.displayDamage;

public class EntityDamage implements Listener {
    final BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();
    static final Map<Player, Long> damageCD = new HashMap<>();
    static final Map<Entity, Long> lastDamageTime = new HashMap<>();

    public static void unload(){
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

        if (event instanceof EntityDamageByEntityEvent) {
            if (helper.isMythicMob(e)) {
                ConfigurationSection mob = Book.getCfg().getMMMob(helper.getMythicMobInstance(e).getType().getInternalName());
                if (mob != null) {
                    Map<StatType, Double> map = new HashMap<>();
                    for (String key : Objects.requireNonNull(mob.getConfigurationSection("defense")).getKeys(false)) {
                        map.put(StatType.valueOf(key.toUpperCase()), mob.getDouble("defense." + key));
                    }
                    damagedByEntity((EntityDamageByEntityEvent) event, map);
                    return;
                }
            }
            damagedByEntity((EntityDamageByEntityEvent) event, Collections.emptyMap());
        }
    }

    protected void damagedByEntity(EntityDamageByEntityEvent event, Map<StatType, Double> def) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

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
            if (inst != null) {
                ConfigurationSection stats = inst.getConfig().getConfigurationSection("stat");
                if (stats == null) throw new IllegalArgumentException("Invalid item!");

                double critChance = stats.contains("CRITICAL") ? stats.getDouble("CRITICAL") : 0.0;

                complexDamage(event, critChance, getItemDamage(stats, inst.getIdentification()), def);
                return;
            }
        }

        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;

            Map<StatType, Double> damages = new HashMap<>();
            PersistentDataContainer container = arrow.getPersistentDataContainer();

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

    public static Map<StatType, Double> getItemDamage(ConfigurationSection stats, ItemIdentification iden) {
        Map<StatType, Double> damage = new HashMap<>();
        Map<StatType, Float> percentage = (iden == null ? new HashMap<>() : iden.getStat());

        for (String stat : stats.getKeys(false)){
            if (stat.startsWith("DAMAGE_")) {
                StatType damageType = StatType.valueOf(stat);
                double dmg = new StatRange(
                        stats.getDouble(stat + ".max") * percentage.getOrDefault(damageType, 1F),
                        stats.getDouble(stat + ".min") * percentage.getOrDefault(damageType, 1F)
                ).calculate();
                damage.put(damageType, dmg);
            }
        }

        return damage;
    }

    public static void complexDamage(EntityDamageByEntityEvent event, double critChance, Map<StatType, Double> dmg, Map<StatType, Double> def) {
        double damage = 0.0;
        LivingEntity damaged = (LivingEntity) event.getEntity();
        Location loc = damaged.getLocation();

        for (StatType damageType : dmg.keySet()) {
            boolean critical = (new Random().nextFloat() < ((critChance + 10) / 100));
            double singleDamage = calculateDamage(
                    dmg.get(damageType),
                    def.getOrDefault(damageType, 0.0),
                    1 + (critical ? 1 : 0)
            );
            damage += singleDamage;
            if (new Random().nextFloat() < 0.45) {
                double power;
                switch (damageType) {
                    case DAMAGE_IGNIS:
                        power = singleDamage / 8;
                        if (power < 5) break;
                        EntityEffect.setBurning(damaged, power, (int) (singleDamage / 6));
                        break;
                    case DAMAGE_PHYSICAL:
                        power = singleDamage / 12;
                        if (power < 5) break;
                        EntityEffect.setBleeding(damaged, power, (int) (singleDamage / 14));
                        break;
                }
            }
            displayDamage(singleDamage, damageType, loc, critical);
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