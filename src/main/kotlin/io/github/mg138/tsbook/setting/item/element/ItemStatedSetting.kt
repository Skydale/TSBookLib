package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.item.attribute.Identifier
import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.util.StatFactory
import org.bukkit.configuration.file.YamlConfiguration

class ItemStatedSetting(setting: ItemSetting, val stats: StatMap) : ItemSetting(setting) {
    constructor(setting: YamlConfiguration) : this(
        ItemSetting(setting, Identifier.PresetKey.item),
        StatFactory.statMap(setting.getConfigurationSection("stat")!!)
    )
}