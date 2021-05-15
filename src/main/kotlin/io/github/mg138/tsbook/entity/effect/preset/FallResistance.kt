package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.EffectType
import io.github.mg138.tsbook.entity.effect.listener.DamageModifier
import io.github.mg138.tsbook.entity.effect.pattern.FlagEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.item.attribute.stat.util.StatUtil
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.max

@Component
class FallResistance : FlagEffect(
    make = { effect, effectProperty, effectManager ->
        ActiveFallResistance(effect, effectProperty, effectManager)
    }
) {
    class ActiveFallResistance(
        effect: Effect,
        property: EffectProperty,
        effectManager: EffectManager
    ) : ActiveFlagEffect(effect, property, effectManager), DamageModifier {
        override fun tick() = this.deactivate()

        override fun onDamage(event: EntityDamageEvent) {
            if (event.cause == EntityDamageEvent.DamageCause.FALL) {
                event.isCancelled = true

                DamageHandler.simpleDamage(
                    event.entity,
                    StatUtil.calculateModifier(
                        event.finalDamage, max((1 - this.property.power), 0.0)
                    )
                )
            }
        }
    }

    override fun getType() = EffectType.PresetTypes.fallResistance
}