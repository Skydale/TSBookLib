package io.github.mg138.tsbook.entity.effect.data

import org.bukkit.entity.Entity

class EntityStatus(val target: Entity, val status: Status) {
    constructor(target: Entity, type: StatusType, power: Double, ticks: Long) : this(
        target, Status(type, power, ticks)
    )
}