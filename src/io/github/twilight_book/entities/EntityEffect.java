package io.github.twilight_book.entities;

import io.github.twilight_book.Book;
import io.github.twilight_book.listener.event.EntityDamage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EntityEffect {
    final static Map<Entity, Map<StatusEffect, BukkitRunnable>> MapEffect = new HashMap<>();

    public static void call(LivingEntity entity, StatusEffect effect, double power, int time) {
        switch (effect) {
            case BURNING:
                setBurning(entity, power, time);
        }
    }

    public static void setBurning(LivingEntity entity, double power, int time) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.damage(entity, power, "ignis") || i > time) {
                    MapEffect.remove(entity);
                    cancel();
                    return;
                }
                entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 6);
                i += 10;
            }
        };
        runnable.runTaskTimer(Book.getInst(), 0, 10);

        if (has(entity, StatusEffect.BURNING)) {
            remove(entity, StatusEffect.BURNING);
        }
        add(entity, StatusEffect.BURNING, runnable);
    }

    public static void setBleeding(LivingEntity entity, double power, int time) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.damage(entity, power, "bleed") || i > time) {
                    MapEffect.remove(entity);
                    cancel();
                    return;
                }

                entity.getWorld().spawnParticle(Particle.BLOCK_DUST, entity.getLocation(), 16, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                i += 7;
            }
        };
        runnable.runTaskTimer(Book.getInst(), 0, 7);

        if (has(entity, StatusEffect.BLEEDING)) {
            remove(entity, StatusEffect.BLEEDING);
        }
        add(entity, StatusEffect.BLEEDING, runnable);
    }

    public static void unload() {
        MapEffect.clear();
    }

    private static void remove(LivingEntity entity, StatusEffect effect) {
        Map<StatusEffect, BukkitRunnable> map = MapEffect.get(entity);
        map.get(effect).cancel();
        map.remove(effect);
    }

    private static void add(LivingEntity entity, StatusEffect effect, BukkitRunnable runnable) {
        Map<StatusEffect, BukkitRunnable> map = MapEffect.getOrDefault(entity, new HashMap<>());
        map.put(effect, runnable);
        MapEffect.putIfAbsent(entity, map);
    }

    private static boolean has(LivingEntity entity, StatusEffect... effects) {
        if (MapEffect.containsKey(entity)) {
            for (StatusEffect effect : effects) {
                if (MapEffect.get(entity).containsKey(effect)) return true;
            }
        }
        return false;
    }
}