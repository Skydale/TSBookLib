package io.github.mg138.tsbook.entity.effect.data

import org.bukkit.entity.Entity

class EntityStatusEffect(val target: Entity, type: StatusEffectType, power: Double, ticks: Long) {
    val statusEffect = StatusEffect(type, power, ticks)
}