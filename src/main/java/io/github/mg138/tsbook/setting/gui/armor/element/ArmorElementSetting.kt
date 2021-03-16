package io.github.mg138.tsbook.setting.gui.armor.element

import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import org.bukkit.Material

class ArmorElementSetting(
    slot: Int,
    material: Material,
    count: Int,
    name: String,
    lore: List<String>,
    model: Int,
    val setting: ArmorSetting
) : GUIElementSetting(slot, material, count, name, lore, model)