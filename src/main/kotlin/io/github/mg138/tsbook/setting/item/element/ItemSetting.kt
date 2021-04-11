package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.attribute.ItemType
import org.bukkit.Material

abstract class ItemSetting(
    val id: String,
    val itemType: ItemType,
    val material: Material,
    val model: Int = 0,
    val name: String,
    val lore: List<String>
) {
    constructor(setting: ItemSetting) : this(
        setting.id,
        setting.itemType,
        setting.material,
        setting.model,
        setting.name,
        setting.lore
    )
}