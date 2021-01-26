package io.github.mg138.tsbook.entities;

import org.bukkit.entity.LivingEntity;

public class EntityEffect {
    LivingEntity target;
    double power;
    int time;

    public EntityEffect(LivingEntity target, double power, int time) {
        this.target = target;
        this.power = power;
        this.time = time;
    }
}
