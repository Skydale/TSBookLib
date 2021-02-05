package io.github.mg138.tsbook.entities.data;

import io.github.mg138.tsbook.entities.EffectHandler;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.listener.event.EntityDamage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.function.Consumer;

public class RegisteredEffects {
    private static void applyEffect(Entity target, StatusEffect effect, BukkitRunnable runnable, int delay, int period) {
        if (!(target instanceof LivingEntity)) return;

        EffectHandler.addRunnable((LivingEntity) target, effect, runnable);
        EffectHandler.addEffect((LivingEntity) target, effect);
        runnable.runTaskTimer(Book.getInst(), delay, period);
    }

    private static final Consumer<EntityStatusEffect> burning = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;
        int period = 10;

        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.simpleDamage(target, statusEffect.power, StatType.DAMAGE_IGNIS) || i > statusEffect.ticks) {
                    EffectHandler.remove(target);
                    cancel();
                    return;
                }

                target.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 6);
                i += period;
            }
        };

        applyEffect(target, statusEffect, runnable, 0, period);
    };

    private static final Consumer<EntityStatusEffect> bleeding = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;
        int period = 7;

        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!EntityDamage.simpleDamage(target, statusEffect.power, StatType.DAMAGE_BLEED) || i > statusEffect.ticks) {
                    EffectHandler.remove(target);
                    cancel();
                    return;
                }

                target.getWorld().spawnParticle(Particle.BLOCK_DUST, target.getLocation(), 20, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                i += period;
            }
        };

        applyEffect(target, statusEffect, runnable, 0, period);
    };

    private static final Consumer<EntityStatusEffect> fall_damage_resistance = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                EffectHandler.remove(target);
                cancel();
            }
        };

        applyEffect(target, statusEffect, runnable, effect.statusEffect.ticks, 0);
    };

    private static HashMap<StatusEffectType, Consumer<EntityStatusEffect>> constructEffects() {
        HashMap<StatusEffectType, Consumer<EntityStatusEffect>> effects = new HashMap<>();
        effects.put(StatusEffectType.BURNING, burning);
        effects.put(StatusEffectType.BLEEDING, bleeding);
        effects.put(StatusEffectType.FALL_DAMAGE_RESISTANCE, fall_damage_resistance);
        return effects;
    }

    public static final HashMap<StatusEffectType, Consumer<EntityStatusEffect>> Effects = constructEffects();
}
