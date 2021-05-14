package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.Status
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.entity.LivingEntity

abstract class DamagingEffect(
    private val type: StatType,
    private val period: Long,
    private val effect: (LivingEntity) -> Unit
) : Effect {
    override fun makeEffect(status: Status, effectManager: EffectManager): ActiveEffect {
        return object : ActiveEffect(this, status.target, 0L, period, effectManager) {
            val duration = status.duration
            val power = status.power

            var i = 0L

            override fun tick() {
                if (i >= duration || !DamageHandler.simpleDamage(entity, power, type)) {
                    cancelAndRemove()
                    return
                }

                effect(entity)

                i += period
            }
        }
    }
}