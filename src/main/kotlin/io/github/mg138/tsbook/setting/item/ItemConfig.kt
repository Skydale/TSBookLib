package io.github.mg138.tsbook.setting.item

import io.github.mg138.tsbook.item.attribute.DefaultIdentifier.*
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting
import java.lang.IllegalArgumentException
import java.util.*

object ItemConfig {
    val items: MutableMap<String, MutableMap<String, ItemSetting>> = HashMap()

    fun unload() {
        items.clear()
    }

    @Throws(IllegalArgumentException::class)
    fun putAll(identifier: String, items: MutableMap<String, ItemSetting>) {
        getItems(identifier).putAll(items)
    }

    fun getAllItemNames() = items.values.flatMap { it.keys }

    @Throws(IllegalArgumentException::class)
    fun getItems(identifier: String) = items[identifier]
        ?: throw IllegalArgumentException("$identifier doesn't exist. Keys: ${items.keys}")

    @Throws(IllegalArgumentException::class)
    fun getItemNames(identifier: String) = getItems(identifier).keys

    @Throws(IllegalArgumentException::class)
    fun getItemNames() = getItemNames(ITEM.identifier)

    @Throws(IllegalArgumentException::class)
    fun getUnidNames() = getItemNames(UNID.identifier)

    @Throws(IllegalArgumentException::class)
    fun getItem(identifier: String, id: String) = getItems(identifier).let {
        it[id] ?: throw IllegalArgumentException("$id doesn't exist. Keys: ${it.keys}")
    }

    @Throws(IllegalArgumentException::class)
    fun getItem(id: String) = getItem(ITEM.identifier, id)

    @Throws(IllegalArgumentException::class)
    fun getUnid(id: String) = getItem(UNID.identifier, id) as UnidentifiedSetting

    @Throws(IllegalArgumentException::class)
    fun getAnyItemByID(id: String): ItemSetting {
        items.forEach { (_, map) ->
            map[id]?.let { return it }
        }
        throw IllegalArgumentException("No such item called $id exists!")
    }
}