package io.github.mg138.tsbook.entities.effect.data

import org.bukkit.entity.Entity

class EntityStatusEffect(val target: Entity, type: StatusEffectType, power: Double, ticks: Int) {
    val statusEffect: StatusEffect = StatusEffect(type, power, ticks)
}