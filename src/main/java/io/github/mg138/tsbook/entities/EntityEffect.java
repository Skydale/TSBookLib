package io.github.mg138.tsbook.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityEffect {
    Entity target;
    double power;
    int time;

    public EntityEffect(Entity target, double power, int time) {
        this.target = target;
        this.power = power;
        this.time = time;
    }
}
