package io.github.twilight_book.event;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.DamageRange;
import io.github.twilight_book.items.ItemUtils;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.twilight_book.event.DamageIndicator.displayDamage;

public class EntityDamage implements Listener {
    final BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ArmorStand) return;

        if (event instanceof EntityDamageByEntityEvent) {
            if (helper.isMythicMob(e)) {
                ActiveMob mythicMob = helper.getMythicMobInstance(e);
                ConfigurationSection mob = Book.getCfg().getMMMob(mythicMob.getType().getInternalName());

                if (mob != null) {
                    Map<String, Double> map = new HashMap<>();
                    for (String key : mob.getConfigurationSection("defense").getKeys(false)) {
                        map.put(key, mob.getDouble("defense." + key));
                    }

                    calculateDamage((EntityDamageByEntityEvent) event, map);
                    return;
                }
            }
            calculateDamage((EntityDamageByEntityEvent) event, Collections.emptyMap());
            return;
        }
        displayDamage(event.getFinalDamage(), "none", e.getWorld(), e.getLocation());
    }

    protected void calculateDamage(EntityDamageByEntityEvent event, Map<String, Double> def) {
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            LivingEntity damaged = (LivingEntity) event.getEntity();

            if (damager.getEquipment() != null && damager.getEquipment().getItemInMainHand().getItemMeta() != null) {
                YamlConfiguration item = Book.getCfg().getItemByID(
                        ItemUtils.getDataTag(
                                Book.getInst(), damager.getEquipment().getItemInMainHand(), "item"
                        )
                );

                if (item != null) {
                    double damage = 0.0;
                    World world = damaged.getWorld();
                    Location loc = damaged.getLocation();

                    Set<String> config = item.getConfigurationSection("stat.damage").getKeys(false);
                    for (String k : config) {
                        DamageRange v = new DamageRange(
                                item.getDouble("stat.damage." + k + ".max"),
                                item.getDouble("stat.damage." + k + ".min")
                        );

                        double temp = v.calculate();
                        if (def.containsKey(k)) {
                            double d = def.get(k);
                            if (d > 0) {
                                temp *= (1 - (d / (d + 70)));
                            } else if (d < 0) {
                                d *= -1;
                                temp *= (1 + (d / (d + 70)));
                            }
                        }
                        damage += temp;
                        displayDamage(temp, k, world, loc);
                    }

                    event.setDamage(damage);
                    return;
                }
            }
            displayDamage(event.getFinalDamage(), "none", damaged.getWorld(), damaged.getLocation());
        }
    }
}