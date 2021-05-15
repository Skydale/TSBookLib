package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager

abstract class FlagEffect(
    private val make: (Effect, EffectProperty, EffectManager) -> ActiveFlagEffect
) : Effect {
    override fun makeEffect(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return make(this, property, effectManager)
    }
}