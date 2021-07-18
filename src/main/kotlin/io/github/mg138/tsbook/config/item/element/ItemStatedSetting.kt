package io.github.mg138.tsbook.config.item.element

import io.github.mg138.tsbook.stat.data.StatMap

class ItemStatedSetting(setting: ItemSetting, val statMap: StatMap) : ItemSetting(setting) {
    /*
    constructor(setting: YamlConfiguration) : this(
        ItemSetting(setting, Identifier.PresetKey.item),
        StatFactory.statMap(setting.getConfigurationSection("stat")!!)
    )
     */
}