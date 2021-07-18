package io.github.mg138.tsbook.item.event

import io.github.mg138.tsbook.event.EventListener
import io.github.mg138.tsbook.item.api.ItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ItemDespawnEvent

object ItemEventListener : EventListener {
    @EventHandler
    fun onItemDespawn(event: ItemDespawnEvent) {
        ItemManager.remove(event.entity.itemStack)
    }
}