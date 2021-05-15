package io.github.mg138.tsbook.entity.effect

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class EntityEffects {
    private val map: MutableMap<LivingEntity, ActiveEffects> = HashMap()

    operator fun get(entity: LivingEntity) = map.computeIfAbsent(entity) { ActiveEffects() }

    operator fun get(entity: LivingEntity, type: EffectType) = this[entity][type]

    fun apply(effect: ActiveEffect) = this[effect.property.target].apply(effect)

    fun has(entity: Entity, type: EffectType): Boolean {
        if (entity !is LivingEntity) return false

        return map[entity]?.has(type) ?: false
    }

    fun hasEffects(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        return map[entity]?.isNotEmpty() ?: false
    }

    fun remove(entity: LivingEntity, type: EffectType) = map[entity]?.remove(type)

    fun removeAll(entity: LivingEntity) {
        map[entity]?.clear()
        map.remove(entity)
    }

    fun clear() {
        map.values.forEach(ActiveEffects::clear)
        map.clear()
    }
}