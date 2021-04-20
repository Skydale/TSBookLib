package io.github.mg138.tsbook.setting.item

import io.github.mg138.tsbook.item.attribute.Identifier
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting
import java.lang.IllegalArgumentException
import java.util.*

object ItemConfig {
    val items: MutableMap<Identifier, ItemSetting> = HashMap()

    fun unload() {
        items.clear()
    }

    fun getAllItemIds() = items.keys

    fun getAllItemNames() = this.getAllItemIds().map { it.name }

    @Throws(IllegalArgumentException::class)
    fun getItemNames(key: String) = this.getAllItemIds().filter { it.sameKey(key) }.map { it.name }

    @Throws(IllegalArgumentException::class)
    fun getItem(id: Identifier) = items[id] ?: throw IllegalArgumentException("$id doesn't exist.")

    @Throws(IllegalArgumentException::class)
    fun getItem(id: String) = this.getItem(Identifier.PresetKey.item(id))

    @Throws(IllegalArgumentException::class)
    fun getUnid(id: String) = this.getItem(Identifier.PresetKey.unid(id)) as UnidentifiedSetting
}