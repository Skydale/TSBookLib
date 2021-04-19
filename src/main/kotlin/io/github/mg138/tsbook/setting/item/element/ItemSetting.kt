package io.github.mg138.tsbook.setting.item.element

import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.util.translate.TranslateUtil
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

open class ItemSetting(
    val id: String,
    val itemType: ItemType,
    val material: Material,
    val model: Int = 0,
    val name: String,
    val lore: List<String>
) {
    init {
        if (material == Material.AIR) throw IllegalArgumentException("Material cannot be air! (ID: $id)")
        if (id.contains(":")) throw IllegalArgumentException("IDs cannot consist of `:`! (ID: $id)")
    }

    constructor(setting: ItemSetting) : this(
        setting.id,
        setting.itemType,
        setting.material,
        setting.model,
        setting.name,
        setting.lore
    )

    constructor(setting: YamlConfiguration) : this(
        setting.getString("ID")
            ?: throw java.lang.IllegalArgumentException("ID cannot be null."),
        setting.getString("ITEM_TYPE")?.let {
            ItemType.of(it)
        } ?: ItemType.UNKNOWN,
        Material.valueOf(
            setting.getString("MATERIAL") ?: throw java.lang.IllegalArgumentException("Material cannot be null")
        ),
        setting.getInt("MODEL"),
        TranslateUtil.get("FORMAT.NAME", null, setting),
        TranslateUtil.getList("FORMAT.LORE", null, setting)
    )
}