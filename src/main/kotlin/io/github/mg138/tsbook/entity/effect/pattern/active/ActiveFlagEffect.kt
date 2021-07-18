package io.github.mg138.tsbook.entity.effect.pattern.active

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.api.EffectManager

abstract class ActiveFlagEffect(
    effect: Effect,
    property: EffectProperty,
    effectManager: EffectManager
) : ActiveEffect(effect, property, 0L, property.duration, effectManager) {
    override fun tick() = this.deactivate()
}