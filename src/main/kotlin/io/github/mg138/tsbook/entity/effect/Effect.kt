package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.status.StatusType

interface Effect {
    fun makeEffect(entityStatus: EntityStatus): RunningEffect
    fun getType(): StatusType
}