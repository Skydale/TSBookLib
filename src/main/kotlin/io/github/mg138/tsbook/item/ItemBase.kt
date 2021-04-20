package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.util.ItemUtil.createItem
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import org.bukkit.inventory.ItemStack
import java.util.*

open class ItemBase(protected val setting: ItemSetting, val uuid: UUID) {
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
    val itemType get() = setting.itemType
    val material get() = setting.material
    val model get() = setting.model

    fun createItem(): ItemStack {
        return createItem(this)
    }
}