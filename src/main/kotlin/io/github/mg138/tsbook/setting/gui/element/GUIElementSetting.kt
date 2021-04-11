package io.github.mg138.tsbook.setting.gui.element

import io.github.mg138.tsbook.util.translate.TranslateUtil
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

open class GUIElementSetting(
    val slot: Int,
    val material: Material,
    val count: Int,
    val name: String,
    val lore: List<String>,
    val model: Int
) {
    constructor(slot: Int, section: ConfigurationSection) : this(
        slot,
        Material.valueOf(section.getString("MATERIAL")!!),
        section.getInt("COUNT"),
        TranslateUtil.get("NAME", null, section),
        TranslateUtil.getList("LORE", null, section),
        section.getInt("MODEL")
    )
}