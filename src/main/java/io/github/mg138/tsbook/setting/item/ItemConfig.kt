package io.github.mg138.tsbook.setting.item

import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.SimpleItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting
import org.bukkit.configuration.file.YamlConfiguration
import java.lang.IllegalArgumentException
import java.util.*

object ItemConfig : AbstractItemConfig() {
    private val unid: MutableMap<String, UnidentifiedSetting> = HashMap()

    fun load(items: Map<String, YamlConfiguration>, unid: Map<String, YamlConfiguration>) {
        items.forEach { (key, setting) ->
            when {
                setting.contains("stats") -> {
                    this.items[key] = StatedItemSetting(setting)
                }
                else -> {
                    this.items[key] = SimpleItemSetting(setting)
                }
            }
        }
        unid.forEach { (key, setting) ->
            this.unid[key] = UnidentifiedSetting(setting)
        }
    }

    fun getUnidentifiedByID(id: String): UnidentifiedSetting? {
        return unid[id]
    }

    fun getAnyItemByID(id: String): ItemSetting {
        return when {
            items.containsKey(id) -> getItemByID(id)!!
            unid.containsKey(id) -> getUnidentifiedByID(id)!!
            else -> throw IllegalArgumentException("No such item exists!")
        }
    }
}