package io.github.mg138.tsbook.config.gui.armor

import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.player.ArcticGlobalDataService
import io.github.mg138.tsbook.player.data.PlayerData
import io.github.mg138.tsbook.config.gui.armor.element.ArmorElementSetting
import io.github.mg138.tsbook.config.gui.armor.element.ArmorSetting
import io.github.mg138.tsbook.config.gui.element.GUIElementSetting
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*

// todo wtf is this

/*
object ArmorGUIConfig : Iterable<MutableMap.MutableEntry<Int, GUIElementSetting>> {
    private val elementSettings: MutableMap<Int, GUIElementSetting> = HashMap()

    operator fun get(key: Int) = elementSettings[key]
    override fun iterator(): Iterator<MutableMap.MutableEntry<Int, GUIElementSetting>> = elementSettings.iterator()

    fun getByType(itemType: ItemType, player: Player): Int {
        val data = ArcticGlobalDataService.inst.getData<PlayerData>(player) ?: return -1

        elementSettings.forEach { (i, setting) ->
            if (!data.equipment.containsKey(i)) {
                if (setting is ArmorElementSetting) {
                    if (setting.setting.itemType == itemType) return i
                }
            }
        }

        return -1
    }

    fun load(yaml: YamlConfiguration) {
        elementSettings.clear()
        for (key in yaml.getKeys(false)) {
            val i = key.toInt()
            val section = yaml.getConfigurationSection(key)!!

            elementSettings[i] = when {
                section.contains("EQUIPMENT") -> ArmorElementSetting(
                    i, section, ArmorSetting(section.getConfigurationSection("EQUIPMENT")!!)
                )
                else -> GUIElementSetting(i, section)
            }
        }
    }

    fun unload() {
        elementSettings.clear()
    }
}

 */