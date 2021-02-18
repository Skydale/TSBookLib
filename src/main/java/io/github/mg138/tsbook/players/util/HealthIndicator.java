package io.github.mg138.tsbook.players.util;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HealthIndicator implements Listener {
    public final static Map<LivingEntity, Map<Player, BossBar>> healthIndicators = new HashMap<>();
    public final static Map<Player, Map<LivingEntity, BukkitRunnable>> removeHealthIndicator = new HashMap<>();

    public static void unload() {
        healthIndicators.forEach((entity, map) -> map.forEach((player, bossBar) -> bossBar.removeAll()));
        healthIndicators.clear();
    }

    public static BossBar updateHealth(LivingEntity entity, Player player, Map<StatType, Double> damages) {
        String customName = entity.getCustomName();
        customName = customName == null ? entity.getName() : customName;
        double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        double health = (Math.round(entity.getHealth() * 10) / 10.0);

        final String[] changeText = { "" };
        final double[] totalDamage = { 0.0 };
        final StatType[] largestType = { null };
        final double[] largestValue = { 0.0 };

        damages.forEach((type, damage) -> {
            if (damage > largestValue[0]) {
                largestValue[0] = damage;
                largestType[0] = type;
            }
            damage = (Math.round(damage * 10) / 10.0);
            totalDamage[0] += damage;
            changeText[0] += (Book.Companion.getCfg().translate.translate("indicator." + type));
        });

        totalDamage[0] = -1 * (Math.round(totalDamage[0] * 10) / 10.0);

        if (totalDamage[0] > 0) {
            changeText[0] = (Book.Companion.getCfg().translate.translateString("&a+") + totalDamage[0] + changeText[0]);
        } else if (totalDamage[0] < 0) {
            changeText[0] = (Book.Companion.getCfg().translate.translate("indicator." + largestType[0]) + totalDamage[0] + " " + changeText[0]);
        } else {
            changeText[0] = "";
        }

        String title = Book.Companion.getCfg().translate.translate("health_indicator.title")
                .replace("[name]", customName)
                .replace("[health]", String.valueOf(health))
                .replace("[max_health]", String.valueOf(maxHealth))
                .replace("[change]", changeText[0]);

        double healthPercentage = health / maxHealth;

        BarColor color;
        if (healthPercentage > 0.6) color = BarColor.GREEN;
        else if (healthPercentage > 0.3) color = BarColor.YELLOW;
        else color = BarColor.RED;

        BossBar bar;
        Map<Player, BossBar> players;
        if (healthIndicators.containsKey(entity)) {
            players = healthIndicators.get(entity);
            if (players.containsKey(player)) {
                bar = players.get(player);
                bar.setColor(color);
                bar.setTitle(title);
            } else {
                bar = Bukkit.createBossBar(
                        title, color, BarStyle.SOLID
                );
            }
        } else {
            players = new HashMap<>();
            bar = Bukkit.createBossBar(
                    title, color, BarStyle.SOLID
            );
        }
        bar.setProgress(healthPercentage);
        players.put(player, bar);
        healthIndicators.put(entity, players);
        return bar;
    }

    public static void showToPlayer(Entity entity, Player player, Map<StatType, Double> damages) {
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) entity;

        if (removeHealthIndicator.containsKey(player)) {
            Map<LivingEntity, BukkitRunnable> runnables = removeHealthIndicator.get(player);
            if (runnables.containsKey(entity)) {
                runnables.get(entity).cancel();
                runnables.remove(entity);
            }
        }

        BossBar bossBar = updateHealth(livingEntity, player, damages);
        bossBar.addPlayer(player);
        BukkitRunnable remove = new BukkitRunnable() {
            @Override
            public void run() {
                if (healthIndicators.containsKey(livingEntity)) {
                    Map<Player, BossBar> players = healthIndicators.get(livingEntity);
                    if (players.containsKey(player)) {
                        players.get(player).removeAll();
                        players.remove(player);
                    }
                }
            }
        };
        Map<LivingEntity, BukkitRunnable> runnables = removeHealthIndicator.getOrDefault(player, new HashMap<>());
        runnables.put(livingEntity, remove);
        removeHealthIndicator.put(player, runnables);
        remove.runTaskLater(Book.inst, 50);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        BukkitRunnable remove = new BukkitRunnable() {
            @Override
            public void run() {
                if (healthIndicators.containsKey(entity)) {
                    healthIndicators.get(entity).forEach((player, bossBar) -> bossBar.removeAll());
                }
                healthIndicators.remove(entity);
            }
        };
        remove.runTaskLater(Book.inst, 20);
    }

    @EventHandler
    public void onEntityDamage(CustomDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        LivingEntity damager = event.getDamager();
        if (damager instanceof Player) {
            showToPlayer(entity, (Player) damager, event.getDamages());
        }
    }
}