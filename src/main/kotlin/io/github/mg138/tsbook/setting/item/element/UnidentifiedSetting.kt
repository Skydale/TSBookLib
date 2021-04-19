package io.github.mg138.tsbook.setting.item.element

import org.bukkit.configuration.file.YamlConfiguration

class UnidentifiedSetting(setting: ItemSetting, val iden: List<String>) : ItemSetting(setting) {
    constructor(setting: YamlConfiguration) : this(
        ItemSetting(setting),
        setting.getStringList("ITEMS")
    )
}