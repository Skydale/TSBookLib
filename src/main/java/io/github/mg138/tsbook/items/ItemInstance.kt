package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.items.ItemUtils.createItem
import io.github.mg138.tsbook.stat.util.map.RegisteredPlaceholder
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemInstance( //represents a single ItemStack
    private val setting: ItemSetting, val itemStats: ItemStats?, val internalType: String, val uuid: UUID
) {
    fun createItem(): ItemStack {
        return createItem(this)
    }

    constructor(ID: String, stats: ItemStats?, internalType: String, uuid: UUID) : this(
        ItemConfig.getAnyItemByID(ID),
        stats,
        internalType,
        uuid
    )

    private fun putStatsInLore() {
        itemStats ?: return

        setting.lore.forEach { s ->
            itemStats.stats.forEach { (type, stat) ->
                RegisteredPlaceholder.placeholders[type]?.let {
                    if (s.contains(it)) s.replace(it, itemStats.translate(type, stat))
                }
            }
        }
    }

    val name: String
        get() = setting.name
    val lore: List<String>
        get() = setting.lore
    val id: String
        get() = setting.id
    val itemType: String
        get() = setting.item_type
    val material: Material
        get() = setting.material
    val model: Int
        get() = setting.model

    init {
        if (itemStats != null) putStatsInLore()
    }
}