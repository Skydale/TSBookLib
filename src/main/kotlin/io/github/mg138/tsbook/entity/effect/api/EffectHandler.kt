package io.github.mg138.tsbook.entity.effect.api

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.data.EffectProperty


object EffectHandler {
    private val registeredEffects: MutableMap<String, Effect> = HashMap()

    val effectIds = registeredEffects.keys
    val effects = registeredEffects.values

    fun register(effect: Effect) {
        registeredEffects[effect.id] = effect
    }

    operator fun get(id: String) = registeredEffects[id]

    fun makeEffect(id: String, property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return this[id]?.activate(property, effectManager)
            ?: throw IllegalArgumentException("Effect of type $id doesn't exist.")
    }

    operator fun plusAssign(effect: Effect) = register(effect)
}