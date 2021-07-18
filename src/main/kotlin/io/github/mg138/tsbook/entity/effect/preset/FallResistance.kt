package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.stat.util.StatUtil
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.type.DamageModifier
import io.github.mg138.tsbook.entity.effect.pattern.FlagEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.max

/*
@Component
class FallResistance : FlagEffect() {
    override val id = "FALL_RESISTANCE"

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
                    StatUtil.damageMod(
                        event.finalDamage, max((1 - this.property.power), 0.0)
                    )
                )
            }
        }
    }

    override fun makeFlagEffect(effect: Effect, property: EffectProperty, effectManager: EffectManager) =
        ActiveFallResistance(effect, property, effectManager)
}
 */