package io.github.mg138.tsbook.entity.effect.data

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class EntityEffectMap {
    private val map: MutableMap<LivingEntity, EffectMap> = HashMap()

    operator fun get(entity: LivingEntity): EffectMap {
        return map.computeIfAbsent(entity) { EffectMap() }
    }

    operator fun get(entity: LivingEntity, id: String): ActiveEffect? {
        return this[entity][id]
    }

    fun activate(effect: ActiveEffect) {
        this[effect.property.target].activate(effect)
    }

    fun has(entity: Entity, id: String): Boolean {
        if (entity !is LivingEntity) return false

        return map[entity]?.has(id) ?: false
    }

    fun hasAny(entity: Entity): Boolean {
        if (entity !is LivingEntity) return false

        return map[entity]?.isNotEmpty() ?: false
    }

    fun remove(entity: LivingEntity, id: String) {
        map[entity]?.remove(id)
    }

    fun removeAll(entity: LivingEntity) {
        map[entity]?.clear()
        map.remove(entity)
    }

    fun clear() {
        map.values.forEach(EffectMap::clear)
        map.clear()
    }
}