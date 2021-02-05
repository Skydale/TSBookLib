package io.github.mg138.tsbook.entities;

import io.github.mg138.tsbook.entities.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.data.RegisteredEffects;
import io.github.mg138.tsbook.entities.data.StatusEffect;
import io.github.mg138.tsbook.entities.data.StatusEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EffectHandler implements Listener {
    private final static HashMap<LivingEntity, HashMap<StatusEffectType, BukkitRunnable>> activeRunnable = new HashMap<>();
    private final static HashMap<LivingEntity, HashMap<StatusEffectType, StatusEffect>> activeEffect = new HashMap<>();

    public static void apply(StatusEffectType type, Entity target, double power, int time) {
        RegisteredEffects.Effects.get(type).accept(new EntityStatusEffect(target, type, power, time));
    }

    public static void unload() {
        activeRunnable.clear();
    }

    public static void addEffect(LivingEntity entity, StatusEffect effect) {
        HashMap<StatusEffectType, StatusEffect> effects;

        if (activeEffect.containsKey(entity)) {
            effects = getEffects(entity);
        } else {
            effects = new HashMap<>();
        }

        effects.put(effect.type, effect);
        activeEffect.put(entity, effects);
    }

    public static StatusEffect getEffect(LivingEntity entity, StatusEffectType type) {
        HashMap<StatusEffectType, StatusEffect> effects = getEffects(entity);
        if (effects == null) return null;

        return effects.get(type);
    }

    public static HashMap<StatusEffectType, StatusEffect> getEffects(LivingEntity entity) {
        return activeEffect.get(entity);
    }

    public static boolean hasEffect(LivingEntity entity, StatusEffectType type) {
        HashMap<StatusEffectType, StatusEffect> effects = getEffects(entity);
        if (effects == null) return false;

        return effects.containsKey(type);
    }

    public static void addRunnable(LivingEntity entity, StatusEffect effect, BukkitRunnable runnable) {
        HashMap<StatusEffectType, BukkitRunnable> runnables;

        if (activeRunnable.containsKey(entity)) {
            runnables = activeRunnable.get(entity);
        } else {
            runnables = new HashMap<>();
        }

        BukkitRunnable old = runnables.put(effect.type, runnable);
        if (old != null) old.cancel();

        activeRunnable.put(entity, runnables);
    }

    public static void remove(LivingEntity entity) {
        activeEffect.remove(entity);
        activeRunnable.get(entity).forEach((effect, runnable) -> runnable.cancel());
        activeRunnable.remove(entity);
    }
}