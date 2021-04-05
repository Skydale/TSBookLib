package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.stat.StatMap
import org.bukkit.configuration.file.YamlConfiguration

class StatedItemSetting(val setting: ItemSetting, val stats: StatMap) : ItemSetting(setting) {
    constructor(setting: YamlConfiguration) : this(
        SimpleItemSetting(setting),
        StatMap.from(setting.getConfigurationSection("stat")!!)
    )
}