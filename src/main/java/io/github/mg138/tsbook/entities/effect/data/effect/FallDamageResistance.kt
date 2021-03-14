package io.github.mg138.tsbook.entities.effect.data.effect

object FallDamageResistance : Effect(
    delay = { it.ticks },
    period = { 0 },
    condition = { _, _ -> true }
)