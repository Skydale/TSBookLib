package io.github.mg138.tsbook.setting.gui.armor

import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.gui.armor.element.ArmorElementSetting
import io.github.mg138.tsbook.setting.gui.armor.element.ArmorSetting
import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import io.github.mg138.tsbook.utils.Translate
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*

object ArmorGUIConfig {
    private const val npeMessage = "Something wrong happened when iterating through the config!"
    private val elementSettings: MutableMap<Int, GUIElementSetting> = HashMap()

    operator fun get(key: Int) = elementSettings[key]

    fun getByType(type: String, player: Player): Int {
        val data = ArcticGlobalDataService.inst.getData<PlayerData>(player) ?: return -1

        elementSettings.forEach { (i, setting) ->
            if (!data.equipment.containsKey(i)) {
                if (setting is ArmorElementSetting) {
                    if (setting.setting.type == type) return i
                }
            }
        }

        return -1
    }

    fun load(yaml: YamlConfiguration, translate: Translate) {
        elementSettings.clear()
        for (key in yaml.getKeys(false)) {
            val i = key.toInt()
            val section = yaml.getConfigurationSection(key) ?: throw NullPointerException(npeMessage)

            val material = Material.valueOf(section.getString("MATERIAL")!!)
            val count = section.getInt("COUNT")
            val name = translate.translate("NAME", null, section)
            val lore = translate.translateList("LORE", null, section)
            val model = section.getInt("MODEL")

            elementSettings[i] = when {
                section.contains("EQUIPMENT") -> {
                    val equipment = section.getConfigurationSection("EQUIPMENT") ?: throw NullPointerException(npeMessage)
                    ArmorElementSetting(i, material, count, name, lore, model, ArmorSetting(equipment))
                }
                else -> GUIElementSetting(i, material, count, name, lore, model)
            }
        }
    }

    fun unload() {
        elementSettings.clear()
    }
}