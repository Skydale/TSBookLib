package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

/*
    Declares the most basic instance of a ItemSetting
 */
class SimpleItemSetting(
    id: String,
    item_type: String,
    material: Material,
    model: Int,
    name: String,
    lore: List<String>
) : ItemSetting(id, item_type, material, model, name, lore) {
    constructor(setting: YamlConfiguration) : this(
        setting.getString("ID")!!,
        setting.getString("ITEM_TYPE")!!,
        Material.valueOf(setting.getString("MATERIAL")!!),
        setting.getInt("MODEL"),
        BookConfig.translate.translate("FORMAT.NAME", null, setting),
        BookConfig.translate.translateList("FORMAT.LORE", null, setting)
    )
}