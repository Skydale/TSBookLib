package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.item.attribute.stat.StatMap
import org.bukkit.configuration.file.YamlConfiguration

class StatedItemSetting(setting: ItemSetting, val stats: StatMap) : ItemSetting(setting) {
    constructor(setting: YamlConfiguration) : this(
        ItemSetting(setting),
        StatMap.from(setting.getConfigurationSection("stat")!!)
    )
}