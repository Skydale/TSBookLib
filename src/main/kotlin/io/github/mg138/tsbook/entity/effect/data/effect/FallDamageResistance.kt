package io.github.mg138.tsbook.entity.effect.data.effect

object FallDamageResistance : SimpleEffectPattern(
    delay = { it.ticks },
    period = { 0 },
    condition = { _, _ -> true }
)