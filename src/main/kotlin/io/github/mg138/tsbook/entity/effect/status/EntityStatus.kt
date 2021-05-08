package io.github.mg138.tsbook.entity.effect.status

import org.bukkit.entity.LivingEntity

class EntityStatus(val target: LivingEntity, val status: Status) {
    constructor(target: LivingEntity, type: StatusType, power: Double, ticks: Long) : this(
        target, Status(type, power, ticks)
    )
}