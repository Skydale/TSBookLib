package io.github.mg138.tsbook.entities.effect.data;

import org.bukkit.entity.Entity;

public class EntityStatusEffect {
    public final Entity target;
    public final StatusEffect statusEffect;

    public EntityStatusEffect(Entity target, StatusEffectType type, double power, int ticks) {
        this.target = target;
        this.statusEffect = new StatusEffect(type, power, ticks);
    }
}
