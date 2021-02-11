package io.github.mg138.tsbook.entities.effect;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.map.RegisteredEffects;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EffectHandler implements Listener {
    private final static HashMap<LivingEntity, HashMap<StatusEffectType, BukkitRunnable>> activeRunnable = new HashMap<>();
    private final static HashMap<LivingEntity, HashMap<StatusEffectType, StatusEffect>> activeEffect = new HashMap<>();

    public static void applyRawEffect(Entity target, StatusEffect effect, BukkitRunnable runnable, int delay, int period) {
        if (!(target instanceof LivingEntity)) return;

        addRunnable((LivingEntity) target, effect, runnable);
        addEffect((LivingEntity) target, effect);
        runnable.runTaskTimer(Book.inst, delay, period);
    }

    public static void apply(StatusEffectType type, Entity target, double power, int time) {
        if (!(target instanceof LivingEntity)) return;

        if (activeRunnable.containsKey(target)) {
            HashMap<StatusEffectType, BukkitRunnable> runnables = activeRunnable.get(target);
            if (runnables.containsKey(type)) {
                runnables.get(type).cancel();
                runnables.remove(type);
            }
        }

        if (activeEffect.containsKey(target)) {
            activeEffect.get(target).remove(type);
        }

        RegisteredEffects.Effects.get(type).accept(new EntityStatusEffect(target, type, power, time));
    }

    public static void unload() {
        HashSet<BukkitRunnable> removing = new HashSet<>();
        activeRunnable.forEach((entity, runnableMap) ->
                runnableMap.forEach((type, runnable) ->
                        removing.add(runnable)
                )
        );
        removing.forEach(BukkitRunnable::cancel);
        activeRunnable.clear();
        activeEffect.clear();
    }

    public static void addEffect(LivingEntity entity, StatusEffect effect) {
        HashMap<StatusEffectType, StatusEffect> effects = activeEffect.getOrDefault(entity, new HashMap<>());

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
        HashMap<StatusEffectType, BukkitRunnable> runnables = activeRunnable.getOrDefault(entity, new HashMap<>());

        BukkitRunnable old = runnables.put(effect.type, runnable);
        if (old != null) old.cancel();

        activeRunnable.put(entity, runnables);
    }

    public static void remove(LivingEntity entity, StatusEffectType type) {
        if (!activeEffect.containsKey(entity) || !activeRunnable.containsKey(entity)) return;
        activeEffect.get(entity).remove(type);
        activeRunnable.get(entity).remove(type);
    }

    public static void remove(LivingEntity entity) {
        HashMap<StatusEffectType, BukkitRunnable> removing;
        try {
            removing = new HashMap<>(activeRunnable.get(entity));
        } catch (NullPointerException e) {
            throw new NullPointerException("The entity doesn't have any active effects");
        }
        removing.forEach((type, runnable) -> runnable.cancel());
        removing.clear();
        activeRunnable.remove(entity);
        activeEffect.remove(entity);
    }
}