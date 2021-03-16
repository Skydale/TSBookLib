package io.github.mg138.tsbook.setting.gui.element

import org.bukkit.Material

open class GUIElementSetting(
    val slot: Int,
    val material: Material,
    val count: Int,
    val name: String,
    val lore: List<String>,
    val model: Int
)