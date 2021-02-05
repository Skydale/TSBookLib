package io.github.mg138.tsbook.entities.data;

import org.bukkit.entity.Entity;

public class EntityStatusEffect {
    Entity target;
    StatusEffect statusEffect;

    public EntityStatusEffect(Entity target, StatusEffectType type, double power, int ticks) {
        this.target = target;
        this.statusEffect = new StatusEffect(type, power, ticks);
    }
}
