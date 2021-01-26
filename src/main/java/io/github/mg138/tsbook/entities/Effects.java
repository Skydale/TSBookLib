package io.github.mg138.tsbook.entities;

import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.listener.event.EntityDamage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Effects {
    private static void applyEffect(LivingEntity target, StatusEffect effect, BukkitRunnable runnable, int delay) {
        EffectHandler.add(target, effect, runnable);
        runnable.runTaskTimer(Book.getInst(), 0, delay);
    }

    private static final Consumer<EntityEffect> burning = effect -> {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.simpleDamage(effect.target, effect.power, StatType.DAMAGE_IGNIS) || i > effect.time) {
                    EffectHandler.remove(effect.target);
                    cancel();
                    return;
                }

                effect.target.getWorld().spawnParticle(Particle.LAVA, effect.target.getLocation(), 6);
                i += 10;
            }
        };

        applyEffect(effect.target, StatusEffect.BURNING, runnable, 10);
    };

    private static final Consumer<EntityEffect> bleeding = effect -> {
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.simpleDamage(effect.target, effect.power, StatType.DAMAGE_BLEED) || i > effect.time) {
                    EffectHandler.remove(effect.target);
                    cancel();
                    return;
                }

                effect.target.getWorld().spawnParticle(Particle.BLOCK_DUST, effect.target.getLocation(), 16, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                i += 7;
            }
        };

        applyEffect(effect.target, StatusEffect.BLEEDING, runnable, 7);
    };

    public static HashMap<StatusEffect, Consumer<EntityEffect>> constructEffects() {
        HashMap<StatusEffect, Consumer<EntityEffect>> effects = new HashMap<>();
        effects.put(StatusEffect.BURNING, burning);
        effects.put(StatusEffect.BLEEDING, bleeding);
        return effects;
    }

    public static final HashMap<StatusEffect, Consumer<EntityEffect>> EFFECTS = constructEffects();
}
