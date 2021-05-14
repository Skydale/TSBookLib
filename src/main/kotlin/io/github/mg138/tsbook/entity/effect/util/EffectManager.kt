package io.github.mg138.tsbook.entity.effect.util

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import io.github.mg138.tsbook.entity.effect.*
import io.github.mg138.tsbook.entity.effect.bridge.EffectHandler
import org.bukkit.entity.LivingEntity

@Component
class EffectManager(
    private val handler: EffectHandler
) : LifeCycleHook {
    private val effects = EntityEffects()

    private fun makeEffect(status: Status) = handler.makeEffect(status, this)

    // Note: this will activate the effect.
    fun apply(effect: ActiveEffect) = effects.apply(effect)

    fun apply(status: Status) = this.apply(makeEffect(status))

    operator fun plusAssign(effect: ActiveEffect) {
        this.apply(effect)
    }

    operator fun plusAssign(status: Status) {
        this.apply(status)
    }

    operator fun get(entity: LivingEntity) = effects[entity]

    operator fun get(entity: LivingEntity, type: EffectType) = effects[entity, type]

    fun has(entity: LivingEntity, type: EffectType) = effects.has(entity, type)

    fun hasEffects(entity: LivingEntity) = effects.hasEffects(entity)

    fun remove(entity: LivingEntity, type: EffectType) = effects.remove(entity, type)

    fun removeAll(entity: LivingEntity) = effects.removeAll(entity)

    override fun onDisable() {
        effects.clear()
    }
}