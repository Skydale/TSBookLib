package io.github.mg138.tsbook.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EffectHandler implements Listener {
    private final static HashMap<Entity, HashMap<StatusEffect, BukkitRunnable>> effects = new HashMap<>();

    public static void apply(StatusEffect effect, Entity entity, double power, int time) {
        Effects.EFFECTS.get(effect).accept(new EntityEffect(entity, power, time));
    }

    public static void unload() {
        effects.clear();
    }

    static void add(Entity entity, StatusEffect effect, BukkitRunnable runnable) {
        if (effects.containsKey(entity)) {
            HashMap<StatusEffect, BukkitRunnable> map = effects.get(entity);
            if (map.containsKey(effect)) {
                map.get(effect).cancel();
                map.put(effect, runnable);
                effects.put(entity, map);
            } else {
                map.put(effect, runnable);
                effects.put(entity, map);
            }
        } else {
            HashMap<StatusEffect, BukkitRunnable> map = new HashMap<>();
            map.put(effect, runnable);
            effects.put(entity, map);
        }
    }

    static void remove(Entity entity) {
        effects.remove(entity);
    }
}