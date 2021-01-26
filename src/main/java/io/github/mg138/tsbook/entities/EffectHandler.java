package io.github.mg138.tsbook.entities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EffectHandler implements Listener {
    private final static HashMap<LivingEntity, HashMap<StatusEffect, BukkitRunnable>> effects = new HashMap<>();

    public static void apply(StatusEffect effect, LivingEntity entity, double power, int time) {
        Effects.EFFECTS.get(effect).accept(new EntityEffect(entity, power, time));
    }

    public static void unload() {
        effects.clear();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        effects.remove(event.getEntity());
    }

    static void add(LivingEntity entity, StatusEffect effect, BukkitRunnable runnable) {
        if (effects.containsKey(entity)) {
            HashMap<StatusEffect, BukkitRunnable> map = effects.get(entity);
            if (map.containsKey(effect)) {
                map.get(effect).cancel();
                map.put(effect, runnable);
            }
        } else {
            HashMap<StatusEffect, BukkitRunnable> map = new HashMap<>();
            map.put(effect, runnable);
            effects.put(entity, map);
        }
    }

    static boolean has(LivingEntity entity, StatusEffect... effects) {
        if (EffectHandler.effects.containsKey(entity)) {
            for (StatusEffect effect : effects) {
                if (EffectHandler.effects.get(entity).containsKey(effect)) return true;
            }
        }
        return false;
    }

    static void remove(LivingEntity entity) {
        effects.get(entity).forEach((effect, runnable) -> runnable.cancel());
        effects.remove(entity);
    }
}