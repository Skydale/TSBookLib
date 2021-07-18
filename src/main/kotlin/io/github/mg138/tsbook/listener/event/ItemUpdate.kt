package io.github.mg138.tsbook.listener.event

import io.github.mg138.tsbook.event.EventListener
import io.github.mg138.tsbook.item.api.ItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object ItemUpdate : EventListener {
    private fun update(itemStack: ItemStack?) {
        if (itemStack == null) return
        val meta = itemStack.itemMeta ?: return
        val item = ItemManager.get(itemStack) ?: return

        itemStack.type = item.getMaterial()
        meta.setCustomModelData(item.getModel())

        itemStack.itemMeta = meta
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        update(event.currentItem)
    }
}