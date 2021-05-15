package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.entity.LivingEntity

abstract class DamagingEffect(
    private val type: StatType,
    private val period: Long,
    private val visualEffect: (LivingEntity) -> Unit
) : Effect {
    private class ActiveDamagingEffect(
        effect: Effect,
        property: EffectProperty,
        period: Long,
        effectManager: EffectManager,
        private val type: StatType,
        private val visualEffect: (LivingEntity) -> Unit
    ) : ActiveEffect(effect, property, period, 0L, effectManager) {
        val duration = property.duration
        val power = property.power
        val target = property.target

        var i = 0L

        override fun tick() {
            if (i >= duration || !DamageHandler.simpleDamage(target, power, type)) {
                cancelAndRemove()
                return
            }

            visualEffect(target)

            i += period
        }
    }

    override fun makeEffect(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return ActiveDamagingEffect(this, property, period, effectManager, type, visualEffect)
    }
}