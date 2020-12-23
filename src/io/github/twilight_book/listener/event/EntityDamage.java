package io.github.twilight_book.listener.event;

import io.github.twilight_book.Book;
import io.github.twilight_book.entities.EntityEffect;
import io.github.twilight_book.items.DamageRange;
import io.github.twilight_book.items.ItemUtils;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.github.twilight_book.listener.event.DamageIndicator.displayDamage;

public class EntityDamage implements Listener {
    final BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();
    static long lastDamageTime = 0;
    static Map<Player, Long> damageCD = new HashMap<>();

    public static void unload(){
        damageCD.clear();
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
                if (System.currentTimeMillis() - lastDamageTime > 50) {
                    damage((LivingEntity) event.getEntity(), 2, "ignis");
                    lastDamageTime = System.currentTimeMillis();
                }
                event.setDamage(0);
                return;
        }
        if (event instanceof EntityDamageByEntityEvent) {

            if (helper.isMythicMob(e)) {
                ConfigurationSection mob = Book.getCfg().getMMMob(helper.getMythicMobInstance(e).getType().getInternalName());
                if (mob != null) {
                    Map<String, Double> map = new HashMap<>();
                    for (String key : mob.getConfigurationSection("defense").getKeys(false)) {
                        map.put(key, mob.getDouble("defense." + key));
                    }
                    damagedByEntity((EntityDamageByEntityEvent) event, map);
                    return;
                }
            }
            damagedByEntity((EntityDamageByEntityEvent) event, Collections.emptyMap());
        }
    }

    protected void damagedByEntity(EntityDamageByEntityEvent event, Map<String, Double> def) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player damager = (Player) event.getDamager();

            if (damager.getEquipment() != null && damager.getEquipment().getItemInMainHand().getItemMeta() != null) {
                YamlConfiguration item = Book.getCfg().getItemByID(
                        ItemUtils.getDataTag(
                                Book.getInst(), damager.getEquipment().getItemInMainHand(), "item"
                        )
                );

                if (item != null) {
                    damagedByPlayer(event, item, def);
                    return;
                }
            }
        }
        displayDamage(event.getFinalDamage(), "none", event.getEntity().getLocation());
    }

    protected void damagedByPlayer(EntityDamageByEntityEvent event, YamlConfiguration item, Map<String, Double> def) {
        ConfigurationSection config = item.getConfigurationSection("stat.damage");
        if (config != null) {
            double damage = 0.0;
            LivingEntity damaged = (LivingEntity) event.getEntity();
            Player damager = (Player) event.getDamager();
            Location loc = damaged.getLocation();

            if (!(damageCD.putIfAbsent(damager, System.currentTimeMillis()) == null)) {
                if ((System.currentTimeMillis() - damageCD.get(damager)) < 500) {
                    event.setCancelled(true);
                    return;
                }
                damageCD.replace(damager, System.currentTimeMillis());
            }

            for (String k : config.getKeys(false)) {
                DamageRange v = new DamageRange(
                        item.getDouble("stat.damage." + k + ".max"),
                        item.getDouble("stat.damage." + k + ".min")
                );
                double randomDamage = v.calculate();
                boolean critical = (new Random().nextFloat() < ((item.getDouble("stat.critical") + 10) / 100));
                double temp = calculateDamage(
                        randomDamage,
                        def.getOrDefault(k, 0.0),
                        1 + (critical ? 1 : 0)
                );
                damage += temp;
                if (new Random().nextFloat() < 0.45 ) {
                    double power;
                    switch (k) {
                        case "ignis":
                            power = temp / 8;
                            if (power < 5) break;
                            EntityEffect.setBurning(damaged, power, (int) (temp / 6));
                            break;
                        case "physical":
                            power = temp / 12;
                            if (power < 5) break;
                            EntityEffect.setBleeding(damaged, power, (int) (temp / 14));
                            break;
                    }
                }
                displayDamage(temp, k, loc, critical);
            }
            event.setDamage(damage);
        }
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

    public static boolean damage(LivingEntity entity, double power, String type) {
        if (entity.isDead()) return false;

        double damage = EntityDamage.calculateDamage(power, 0, 1);
        entity.damage(damage);
        entity.setNoDamageTicks(0);
        displayDamage(damage, type, entity.getLocation());
        return true;
    }
}