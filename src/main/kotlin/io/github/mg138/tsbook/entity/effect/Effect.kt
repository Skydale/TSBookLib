package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty

interface Effect {
    val id: String

    fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect
}