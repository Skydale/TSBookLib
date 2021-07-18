package io.github.mg138.tsbook.config.item.element

class UnidentifiedSetting(setting: ItemSetting, val iden: List<String>) : ItemSetting(setting) {
    /*
    constructor(setting: YamlConfiguration) : this(
        ItemSetting(setting, Identifier.PresetKey.unid),
        setting.getStringList("ITEMS")
    )
    */
}