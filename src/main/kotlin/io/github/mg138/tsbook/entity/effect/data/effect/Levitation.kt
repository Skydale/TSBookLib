package io.github.mg138.tsbook.entity.effect.data.effect

import org.bukkit.potion.PotionEffectType

object Levitation : SimpleEffectPattern(
    delay = { it.ticks },
    period = { 0 },
    condition = { _, _ -> true },
    whenExpire = { target, _ -> target.removePotionEffect(PotionEffectType.LEVITATION) }
)