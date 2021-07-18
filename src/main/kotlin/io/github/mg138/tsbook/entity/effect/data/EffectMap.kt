package io.github.mg138.tsbook.entity.effect.data

import io.github.mg138.tsbook.entity.effect.ActiveEffect

class EffectMap : Iterable<Map.Entry<String, ActiveEffect>> {
    private val map: MutableMap<String, ActiveEffect> = HashMap()

    val ids = map.keys
    val effects = map.values

    fun activate(effect: ActiveEffect): ActiveEffect? {
        val id = effect.id
        this.remove(id)

        effect.activate()

        return map.put(id, effect)
    }

    operator fun get(id: String) = map[id]

    fun has(id: String) = map.containsKey(id)

    fun isEmpty() = map.isEmpty()
    fun isNotEmpty() = !isEmpty()

    fun remove(id: String) {
        map[id]?.deactivate()
        map.remove(id)
    }

    fun clear() {
        effects.forEach(ActiveEffect::deactivate)
        map.clear()
    }

    override fun iterator() = map.iterator()

    operator fun plusAssign(effect: ActiveEffect) {
        this.activate(effect)
    }
}