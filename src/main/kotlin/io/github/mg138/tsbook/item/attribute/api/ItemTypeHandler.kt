package io.github.mg138.tsbook.item.attribute.api

import io.github.mg138.tsbook.item.attribute.ItemType

object ItemTypeHandler {
    private val map: MutableMap<String, ItemType> = HashMap()

    fun register(type: ItemType) {
        map[type.id] = type
    }

    operator fun get(id: String) = map[id]
}