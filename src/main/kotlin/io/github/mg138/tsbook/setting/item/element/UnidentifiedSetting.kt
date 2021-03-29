package io.github.mg138.tsbook.setting.item.element

import org.bukkit.configuration.file.YamlConfiguration

class UnidentifiedSetting(val setting: ItemSetting, val iden: List<String>) : ItemSetting(setting) {
    constructor(setting: YamlConfiguration) : this(
        SimpleItemSetting(setting),
        setting.getStringList("ITEMS")
    )
}