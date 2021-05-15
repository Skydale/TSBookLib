package io.github.mg138.tsbook.entity.effect.bridge

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.injection.components.Components
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.EffectType
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.util.EffectManager

@Component
class EffectHandler(
    effects: Components<Effect>
) {
    private val registeredEffects: Map<EffectType, Effect> = effects.associateBy { it.getType() }

    val statusTypes = registeredEffects.keys

    val statusNames = this.statusTypes.map { it.name }

    fun effects() = registeredEffects.values

    operator fun get(type: EffectType) = registeredEffects[type]

    fun makeEffect(type: EffectType, property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return this[type]?.makeEffect(property, effectManager)
            ?: throw IllegalArgumentException("Effect of type $type doesn't exist.")
    }
}