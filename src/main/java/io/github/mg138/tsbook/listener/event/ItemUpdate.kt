package io.github.mg138.tsbook.listener.event

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemUtils.getInstByItem
import io.github.mg138.tsbook.items.ItemUtils.hasItemID
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ItemUpdate : Listener {
    private fun update(item: ItemStack?) {
        if (item == null) return
        if (hasItemID(item)) {
            val inst = getInstByItem(Book.inst, item) ?: throw NullPointerException("Cannot create item instance.")
            item.type = inst.material
            val meta = item.itemMeta ?: throw NullPointerException("Somehow, I cannot get the metadata of the item.")
            meta.setCustomModelData(inst.model)
            item.itemMeta = meta
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        update(event.currentItem)
    }
}