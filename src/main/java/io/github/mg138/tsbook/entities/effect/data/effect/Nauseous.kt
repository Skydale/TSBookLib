package io.github.mg138.tsbook.entities.effect.data.effect

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Nauseous : Effect(
    delay = { it.ticks },
    period = { 0 },
    runBefore = { target, statusEffect ->
        target.removePotionEffect(PotionEffectType.CONFUSION)
        target.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 2147483647, statusEffect.power.toInt()))
    },
    condition = { _, _ -> true },
    whenExpire = { target, _, _ -> target.removePotionEffect(PotionEffectType.CONFUSION) }
)