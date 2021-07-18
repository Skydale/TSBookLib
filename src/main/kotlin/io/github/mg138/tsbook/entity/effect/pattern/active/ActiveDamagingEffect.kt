package io.github.mg138.tsbook.entity.effect.pattern.active

import io.github.mg138.tsbook.stat.type.DamageType
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.pattern.DamagingEffect
import org.bukkit.entity.LivingEntity

/*
class ActiveDamagingEffect(
    effect: DamagingEffect,
    property: EffectProperty,
    period: Long,
    effectManager: EffectManager,
    private val damageType: DamageType,
    private val visualEffect: (LivingEntity) -> Unit
) : ActiveEffect(effect, property, period, 0L, effectManager) {
    val duration = property.duration
    val power = property.power
    val target = property.target

    var i = 0L

    override fun tick() {
        if (i >= duration || !DamageHandler.simpleDamage(target, power, damageType)) {
            stop()
            return
        }

        visualEffect(target)

        i += period
    }
}
 */