package io.github.mg138.tsbook.setting.gui.armor

import io.github.mg138.tsbook.setting.gui.armor.element.ArmorSetting
import io.github.mg138.tsbook.setting.gui.armor.element.ArmorElementSetting
import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import io.github.mg138.tsbook.utils.Translate
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.util.*

object ArmorGUIConfig {
    private const val npeMessage = "Something wrong happened when iterating through the config!"
    val elementSettings: MutableMap<Int, GUIElementSetting> = HashMap()

    fun load(yaml: YamlConfiguration, translate: Translate) {
        elementSettings.clear()
        val keys = yaml.getKeys(false)
        for (key in keys) {
            val i = key.toInt()
            val section = yaml.getConfigurationSection(key) ?: throw NullPointerException(npeMessage)

            val material = Material.valueOf(section.getString("MATERIAL")!!)
            val count = section.getInt("COUNT")
            val name = translate.translate("NAME", null, section)
            val lore = translate.translateList("LORE", null, section) ?: emptyList()
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
}