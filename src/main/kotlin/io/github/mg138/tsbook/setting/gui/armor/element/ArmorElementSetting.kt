package io.github.mg138.tsbook.setting.gui.armor.element

import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import io.github.mg138.tsbook.util.translate.TranslateUtil
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
    constructor(slot: Int, section: ConfigurationSection, armorSetting: ArmorSetting) : this(
        slot,
        Material.valueOf(section.getString("MATERIAL")!!),
        section.getInt("COUNT"),
        TranslateUtil.get("NAME", null, section),
        TranslateUtil.getList("LORE", null, section),
        section.getInt("MODEL"),
        armorSetting
    )
}