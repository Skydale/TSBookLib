package io.github.mg138.tsbook.entity.effect

class ActiveEffects : Iterable<Map.Entry<EffectType, ActiveEffect>> {
    private val map: MutableMap<EffectType, ActiveEffect> = HashMap()

    val types = map.keys

    operator fun get(type: EffectType) = map[type]

    fun apply(effect: ActiveEffect): ActiveEffect? {
        val type = effect.effectType

        this.remove(type)

        effect.activate()

        return map.put(type, effect)
    }

    operator fun plusAssign(effect: ActiveEffect) {
        this.apply(effect)
    }

    fun has(type: EffectType) = map.containsKey(type)

    fun remove(type: EffectType) {
        map[type]?.deactivate()
        map.remove(type)
    }

    fun clear() {
        map.values.forEach(ActiveEffect::deactivate)
        map.clear()
    }

    fun isEmpty() = map.isEmpty()

    fun isNotEmpty() = !isEmpty()

    override fun iterator() = map.iterator()
}