package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.stat.type.DamageType
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveDamagingEffect
import org.bukkit.entity.LivingEntity

abstract class DamagingEffect(
    private val damageType: DamageType,
    private val period: Long
) : Effect {
    open val visualEffect: (LivingEntity) -> Unit = {}

    override fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return ActiveDamagingEffect(this, property, period, effectManager, damageType, visualEffect)
    }
}