package io.github.mg138.tsbook.setting.item.element

import org.bukkit.Material

abstract class ItemSetting(
    val id: String,
    val item_type: String = "NONE",
    val material: Material,
    val model: Int = 0,
    val name: String,
    val lore: List<String>
) {
    constructor(setting: ItemSetting) : this(
        setting.id,
        setting.item_type,
        setting.material,
        setting.model,
        setting.name,
        setting.lore
    )
}