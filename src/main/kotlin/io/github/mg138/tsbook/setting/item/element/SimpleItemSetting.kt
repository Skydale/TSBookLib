package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.attribute.ItemType
import io.github.mg138.tsbook.util.translate.TranslateUtil
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.lang.IllegalArgumentException

/*
    Declares the most basic instance of a ItemSetting
 */
class SimpleItemSetting(
    id: String,
    itemType: ItemType,
    material: Material,
    model: Int,
    name: String,
    lore: List<String>
) : ItemSetting(id, itemType, material, model, name, lore) {
    constructor(setting: YamlConfiguration) : this(
        setting.getString("ID") ?: throw IllegalArgumentException("ID cannot be null."),
        setting.getString("ITEM_TYPE")?.let { ItemType.of(it) } ?: ItemType.UNKNOWN,
        Material.valueOf(setting.getString("MATERIAL") ?: throw IllegalArgumentException("Material cannot be null")),
        setting.getInt("MODEL"),
        TranslateUtil.get("FORMAT.NAME", null, setting),
        TranslateUtil.getList("FORMAT.LORE", null, setting)
    )
}