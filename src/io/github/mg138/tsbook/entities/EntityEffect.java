package io.github.mg138.tsbook.entities;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.data.StatType;
import io.github.mg138.tsbook.listener.event.EntityDamage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EntityEffect {
    final static Map<Entity, Map<StatusEffect, BukkitRunnable>> effects = new HashMap<>();

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
                if (!EntityDamage.simpleDamage(entity, power, StatType.DAMAGE_IGNIS) || i > time) {
                    effects.remove(entity);
                    cancel();
                    return;
                }
                entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 6);
                i += 10;
            }
        };

        if (has(entity, StatusEffect.BURNING)) {
            remove(entity, StatusEffect.BURNING);
        }
        add(entity, StatusEffect.BURNING, runnable);

        runnable.runTaskTimer(Book.getInst(), 0, 10);
    }

    public static void setBleeding(LivingEntity entity, double power, int time) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.simpleDamage(entity, power, StatType.DAMAGE_BLEED) || i > time) {
                    effects.remove(entity);
                    cancel();
                    return;
                }

                entity.getWorld().spawnParticle(Particle.BLOCK_DUST, entity.getLocation(), 16, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                i += 7;
            }
        };

        if (has(entity, StatusEffect.BLEEDING)) {
            remove(entity, StatusEffect.BLEEDING);
        }
        add(entity, StatusEffect.BLEEDING, runnable);

        runnable.runTaskTimer(Book.getInst(), 0, 7);
    }

    public static void unload() {
        effects.clear();
    }

    private static void remove(LivingEntity entity, StatusEffect effect) {
        Map<StatusEffect, BukkitRunnable> map = effects.get(entity);
        map.get(effect).cancel();
        map.remove(effect);
    }

    private static void add(LivingEntity entity, StatusEffect effect, BukkitRunnable runnable) {
        Map<StatusEffect, BukkitRunnable> map = effects.getOrDefault(entity, new HashMap<>());
        map.put(effect, runnable);
        effects.putIfAbsent(entity, map);
    }

    private static boolean has(LivingEntity entity, StatusEffect... effects) {
        if (EntityEffect.effects.containsKey(entity)) {
            for (StatusEffect effect : effects) {
                if (EntityEffect.effects.get(entity).containsKey(effect)) return true;
            }
        }
        return false;
    }
}