package io.github.mg138.tsbook.entity.effect

import org.bukkit.entity.LivingEntity

class Status(val target: LivingEntity, val type: EffectType, val power: Double, val duration: Long)