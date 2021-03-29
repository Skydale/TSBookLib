package io.github.mg138.tsbook.listener.event

import io.github.mg138.tsbook.util.MaterialUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent

class DisableArmorAndOffhand : Listener {

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        val item = event.item ?: return
        if (item.type == Material.AIR) return
        if (MaterialUtil.isArmor(item.type)) {
            event.isCancelled = true
            event.setUseItemInHand(Event.Result.DENY)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player !is Player) return

        when (event.inventory.type) {
            InventoryType.CRAFTING, InventoryType.PLAYER -> Unit
            else -> return
        }
        val slot = event.rawSlot

        when (event.action) {
            InventoryAction.MOVE_TO_OTHER_INVENTORY -> {
                val item = event.currentItem ?: return

                if (MaterialUtil.isArmor(item.type)) event.isCancelled = true
            }
            InventoryAction.HOTBAR_SWAP -> {
                if (event.hotbarButton < 0) event.isCancelled = true
            }
            else -> if (slot in 5..8 || slot == 45) event.isCancelled = true
        }
        player.updateInventory()
    }

    @EventHandler
    fun onPlayerInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked
        if (player !is Player) return

        when (event.inventory.type) {
            InventoryType.CRAFTING, InventoryType.PLAYER -> Unit
            else -> return
        }

        val slots = event.rawSlots

        slots.forEach { slot ->
            if (slot in 5..8 || slot == 45) {
                event.isCancelled = true
            }
        }
        player.updateInventory()
    }
}