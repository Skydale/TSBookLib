package io.github.mg138.tsbook.item.api

import io.github.mg138.tsbook.item.item.factory.ItemFactory

object ItemHandler {
    private val registeredFactories: MutableMap<String, ItemFactory<*>> = HashMap()

    operator fun get(namespace: String) = registeredFactories[namespace]

    fun register(factory: ItemFactory<*>) {
        registeredFactories[factory.namespace] = factory
    }

    fun namespaces() = registeredFactories.keys

    fun factories() = registeredFactories.values
}