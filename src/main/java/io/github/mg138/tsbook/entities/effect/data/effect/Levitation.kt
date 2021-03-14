package io.github.mg138.tsbook.entities.effect.data.effect

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Levitation : Effect(
    delay = { it.ticks },
    period = { 0 },
    runBefore = { target, statusEffect ->
        target.removePotionEffect(PotionEffectType.LEVITATION)
        target.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 2147483647, statusEffect.power.toInt()))
    },
    condition = { _, _ -> true },
    whenExpire = { target, _, _ -> target.removePotionEffect(PotionEffectType.LEVITATION) }
)