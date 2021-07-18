package io.github.mg138.tsbook.config.item.element

import io.github.mg138.tsbook.data.Identifier
import io.github.mg138.tsbook.item.attribute.ItemType
import org.bukkit.Material

/* //TODO fucking doesn't work now
    constructor(setting: YamlConfiguration, key: String) : this(
        setting.getString("ID")?.let {
            Identifier(key, it)
        } ?: throw IllegalArgumentException("Setting doesn't contain an ID."),
        setting.getString("ITEM_TYPE")?.let {
            ItemType.of(it)
        } ?: ItemType.UNKNOWN,
        setting.getString("MATERIAL")?.let {
            Material.valueOf(it)
        } ?: throw IllegalArgumentException("Setting doesn't contain Material."),
        setting.getInt("MODEL"),
        TranslateUtil.get("FORMAT.NAME", null, setting),
        TranslateUtil.getList("FORMAT.LORE", null, setting).toMutableList()
    )
 */

open class ItemSetting(
    val itemId: Identifier,
    val itemType: ItemType,
    val material: Material,
    val model: Int = 0,
    val name: String,
    val lore: MutableList<String>
) {
    init {
        if (material == Material.AIR) throw IllegalArgumentException("Material cannot be air! (ID: $itemId)")
    }

    constructor(setting: ItemSetting) : this(
        setting.itemId,
        setting.itemType,
        setting.material,
        setting.model,
        setting.name,
        setting.lore
    )
}