package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemUtils.createItem
import io.github.mg138.tsbook.items.data.stat.util.map.RegisteredPlaceholder
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemInstance( //represents a single ItemStack
    private val setting: ItemSetting, val stats: ItemStats?, val internalType: String, val uuid: UUID
) {
    fun createItem(): ItemStack {
        return createItem(this)
    }

    constructor(ID: String, stats: ItemStats?, internalType: String, uuid: UUID) : this(
        Book.setting.itemConfig.getAnyItemByID(ID),
        stats,
        internalType,
        uuid
    )

    private fun putStatsInLore() {
        val iterator = setting.lore.listIterator()
        while (iterator.hasNext()) {
            val s = iterator.next()
            stats!!.statMap.forEach { (type, stat) ->
                val placeholder = RegisteredPlaceholder.placeholders[type]!!
                if (s.contains(placeholder)) iterator.set(s.replace(placeholder, stats.translate(type, stat)))
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
        if (stats != null) putStatsInLore()
    }
}