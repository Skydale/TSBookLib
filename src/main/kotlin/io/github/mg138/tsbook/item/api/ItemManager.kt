package io.github.mg138.tsbook.item.api

import io.github.mg138.tsbook.config.item.element.ItemSetting
import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.item.item.factory.ItemFactory
import io.github.mg138.tsbook.item.util.ItemUtil
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

object ItemManager {
    private val itemCache: MutableMap<UUID, ItemBase> = HashMap()

    private fun checkDuplicate(item: ItemBase) {
        while (itemCache.containsKey(item.getUUID())) {
            item.newUUID()
        }
    }

    fun getItemFactory(type: ItemType): ItemFactory<*> {
        return ItemHandler[type] ?: throw IllegalArgumentException("No ItemFactory of type ${type.id} exists!")
    }

    fun generateItem(setting: ItemSetting, dataMap: DataMap?) =
        this.getItemFactory(setting.itemType).makeItem(setting, dataMap)

    private fun add(item: ItemBase) {
        this.itemCache[item.getUUID()] = item
    }

    fun registerItem(item: ItemBase, meta: ItemMeta) {
        this.checkDuplicate(item)

        ItemUtil.setUUID(meta, item)
        this.add(item)
    }

    fun createItemStack(item: ItemBase): ItemStack {
        val itemStack = ItemStack(item.getMaterial())
        val meta = itemStack.itemMeta!! // item can't be Air

        meta.setCustomModelData(item.getModel())

        this.registerItem(item, meta)

        itemStack.itemMeta = meta
        return itemStack
    }

    fun get(itemStack: ItemStack): ItemBase? {
        val meta = itemStack.itemMeta ?: return null

        return ItemUtil.getUUID(meta)?.let {
            itemCache[it]
        }
    }

    fun has(itemStack: ItemStack): Boolean {
        return this.get(itemStack) == null
    }

    fun remove(itemStack: ItemStack) {
        val meta = itemStack.itemMeta ?: return

        this.itemCache.remove(ItemUtil.getUUID(meta))
    }
}