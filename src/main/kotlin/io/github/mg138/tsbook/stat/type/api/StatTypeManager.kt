package io.github.mg138.tsbook.stat.type.api

import io.github.mg138.tsbook.stat.type.StatType

object StatTypeManager {
    private val map: MutableMap<String, StatType> = HashMap()

    val cache: MutableMap<Class<*>, List<*>> = HashMap()

    val registeredTypes = map.values

    inline fun <reified T: Any> filter(): List<T> {
        @Suppress("UNCHECKED_CAST")
        return cache.computeIfAbsent(T::class.java) {
            this.registeredTypes.filterIsInstance<T>()
        } as List<T>
    }

    operator fun get(string: String) = map[string]

    fun register(type: StatType) {
        val id = type.identifier
        if (map.containsKey(id)) throw IllegalArgumentException("Duplicate StatType of id $id!")

        map[id] = type
    }

    operator fun plusAssign(type: StatType) = register(type)
}