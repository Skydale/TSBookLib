package io.github.mg138.tsbook.config.gui.element

import io.github.mg138.tsbook.util.translate.TranslateUtil
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

/*
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
        TranslateUtil.get(section, "NAME", null),
        TranslateUtil.getList(section, "LORE", null),
        section.getInt("MODEL")
    )
}
 */