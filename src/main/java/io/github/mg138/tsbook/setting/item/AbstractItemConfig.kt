package io.github.mg138.tsbook.setting.item

import io.github.mg138.tsbook.setting.item.element.ItemSetting
import java.util.*

abstract class AbstractItemConfig {
    protected val items: MutableMap<String, ItemSetting> = HashMap()

    fun unload() {
        items.clear()
    }

    fun getItems(): Set<String> {
        return items.keys
    }

    fun getItemByID(id: String): ItemSetting? {
        return items[id]
    }
}