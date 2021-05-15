package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.entity.effect.util.EffectManager

interface Effect {
    fun makeEffect(property: EffectProperty, effectManager: EffectManager): ActiveEffect
    fun getType(): EffectType
}