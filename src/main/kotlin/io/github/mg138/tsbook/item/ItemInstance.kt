package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.ItemUtils.createItem
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.stat.util.StatTables
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemInstance ( //represents a single ItemStack
    private val setting: ItemSetting, val itemStat: ItemStat?, val internalType: String, val uuid: UUID
) {
    override fun toString(): String {
        return "{" +
                "\n  display: {" +
                "\n    name: $name," +
                "\n    lore: $lore" +
                "\n  }" +
                "\n}"
    }

    val name = setting.name
    val lore = setting.lore.toMutableList()
    val id get() = setting.id
    val itemType get() = setting.item_type
    val material get() = setting.material
    val model get() = setting.model

    fun createItem(): ItemStack {
        return createItem(this)
    }

    constructor(ID: String, itemStat: ItemStat?, internalType: String, uuid: UUID) : this(
        ItemConfig.getAnyItemByID(ID),
        itemStat,
        internalType,
        uuid
    )

    init {
        putStatsInLore()
        println(this)
    }

    private fun putStatsInLore() {
        itemStat ?: return

        for (i in lore.indices) {
            val s = lore[i]
            itemStat.forEach { (type, _) ->
                StatTables.placeholders[type]?.let {
                    if (s.contains(it)) {
                        lore[i] = s.replace(it, itemStat.translate(type))
                        println(itemStat.translate(type))
                    }
                }
            }
        }
    }
}