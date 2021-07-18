package io.github.mg138.tsbook.entity.effect.api

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.data.EffectMap
import io.github.mg138.tsbook.entity.effect.data.EntityEffectMap
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object EffectManager {
    private val effects = EntityEffectMap()

    private fun makeEffect(id: String, property: EffectProperty): ActiveEffect {
        return EffectHandler.makeEffect(id, property, this)
    }

    inline fun <reified T : Any> filterEffects(entity: LivingEntity): Collection<T> {
        return this.getAll(entity)
            .filterIsInstance<T>()
    }

    fun activate(effect: ActiveEffect) {
        this.effects.activate(effect)
    }

    fun activate(id: String, property: EffectProperty) {
        this.activate(makeEffect(id, property))
    }

    fun activate(id: String, target: LivingEntity, power: Double, duration: Long) {
        this.activate(id, EffectProperty(target, power, duration))
    }

    operator fun get(entity: LivingEntity): EffectMap {
        return this.effects[entity]
    }

    operator fun get(entity: LivingEntity, id: String): ActiveEffect? {
        return this[entity][id]
    }

    fun getAll(entity: LivingEntity): Collection<ActiveEffect> {
        return this[entity].effects
    }

    fun has(entity: Entity, id: String) = this.effects.has(entity, id)
    fun hasAny(entity: Entity) = this.effects.hasAny(entity)
    fun hasNone(entity: Entity) = !hasAny(entity)

    fun remove(entity: LivingEntity, id: String) = this.effects.remove(entity, id)
    fun removeAll(entity: LivingEntity) = this.effects.removeAll(entity)

    operator fun plusAssign(effect: ActiveEffect) {
        this.activate(effect)
    }
}