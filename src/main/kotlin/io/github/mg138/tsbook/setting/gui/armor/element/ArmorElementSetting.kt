package io.github.mg138.tsbook.setting.gui.armor.element

import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import io.github.mg138.tsbook.util.Translate
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

class ArmorElementSetting(
    slot: Int,
    material: Material,
    count: Int,
    name: String,
    lore: List<String>,
    model: Int,
    val setting: ArmorSetting
) : GUIElementSetting(slot, material, count, name, lore, model) {
    constructor(slot: Int, section: ConfigurationSection, armorSetting: ArmorSetting, translate: Translate) : this(
        slot,
        Material.valueOf(section.getString("MATERIAL")!!),
        section.getInt("COUNT"),
        translate.translate("NAME", null, section),
        translate.translateList("LORE", null, section),
        section.getInt("MODEL"),
        armorSetting
    )
}