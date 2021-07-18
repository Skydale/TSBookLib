package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.api.EffectManager

abstract class FlagEffect : Effect {
    abstract fun makeFlagEffect(effect: Effect, property: EffectProperty, effectManager: EffectManager): ActiveFlagEffect

    override fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return this.makeFlagEffect(this, property, effectManager)
    }
}