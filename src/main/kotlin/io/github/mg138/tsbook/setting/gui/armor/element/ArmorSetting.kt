package io.github.mg138.tsbook.setting.gui.armor.element

import io.github.mg138.tsbook.item.attribute.ItemType
import org.bukkit.configuration.ConfigurationSection

class ArmorSetting(val itemType: ItemType) {
    constructor(section: ConfigurationSection) : this(
        section.getString("TYPE")!!.let {
            ItemType.of(it) ?: throw IllegalArgumentException("No ItemType of $it")
        }
    )
}