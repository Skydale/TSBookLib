package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.lang.IllegalArgumentException

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
        setting.getString("ID") ?: throw IllegalArgumentException("ID cannot be null."),
        setting.getString("ITEM_TYPE") ?: "UNKNOWN_TYPE",
        Material.valueOf(setting.getString("MATERIAL") ?: throw IllegalArgumentException("Material cannot be null")),
        setting.getInt("MODEL"),
        BookConfig.language.get("FORMAT.NAME", null, setting),
        BookConfig.language.getList("FORMAT.LORE", null, setting)
    )
}