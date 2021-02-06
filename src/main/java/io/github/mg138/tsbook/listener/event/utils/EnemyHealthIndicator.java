package io.github.mg138.tsbook.listener.event.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class EnemyHealthIndicator implements Listener {
    public static HashMap<LivingEntity, HashSet<Player>> showing = new HashMap<>();
    public static HashMap<LivingEntity, BossBar> healthIndicators = new HashMap<>();

    public static void unload() {
        showing.clear();
        healthIndicators.forEach((entity, bossBar) -> {
            bossBar.removeAll();
        });
        healthIndicators.clear();
    }

    public static BossBar updateHealth(LivingEntity entity) {
        String name = entity.getCustomName();
        name = (name == null) ? "無名生物" : name;

        double healthPercentage = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue() / entity.getHealth();

        BarColor color;
        if (healthPercentage > 0.6) color = BarColor.GREEN;
        else if (healthPercentage > 0.3) color = BarColor.YELLOW;
        else color = BarColor.RED;

        BossBar bossBar;
        if (healthIndicators.containsKey(entity)) {
            bossBar = healthIndicators.get(entity);
        } else {
            bossBar = Bukkit.createBossBar(
                    name, color, BarStyle.SEGMENTED_12
            );
        }
        bossBar.setProgress(healthPercentage);
        healthIndicators.put(entity, bossBar);

        return healthIndicators.get(entity);
    }

    public static void showToPlayer(LivingEntity entity, Player player) {
        BossBar bossBar = updateHealth(entity);
        bossBar.addPlayer(player);

        showing.putIfAbsent(entity, new HashSet<>());
        HashSet<Player> players = showing.get(entity);
        players.add(player);
        showing.put(entity, players);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        showing.remove(entity);

        if (healthIndicators.containsKey(entity)) {
            healthIndicators.get(entity).removeAll();
        }
        healthIndicators.remove(entity);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) entity;

        updateHealth(livingEntity);
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                showToPlayer(livingEntity, (Player) damager);
            }
        }
    }
}
