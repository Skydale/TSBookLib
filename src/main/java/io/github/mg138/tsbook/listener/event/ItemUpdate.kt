package io.github.mg138.tsbook.listener.event

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemUtils.getStringTag
import io.github.mg138.tsbook.items.ItemUtils.getUUID
import tech.clayclaw.arcticglobal.service.PlayerDataService.getData
import io.github.mg138.tsbook.players.ArcticGlobalDataService.Companion.playerDataRef
import io.github.mg138.tsbook.players.data.PlayerData.equipment
import io.github.mg138.tsbook.items.ItemInstance.uuid
import io.github.mg138.tsbook.items.ItemUtils.hasItemID
import io.github.mg138.tsbook.items.ItemUtils.getInstByItem
import io.github.mg138.tsbook.items.ItemInstance.material
import io.github.mg138.tsbook.items.ItemInstance.model
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import io.github.mg138.tsbook.items.ItemUtils
import org.bukkit.event.player.PlayerInteractEvent
import io.github.mg138.tsbook.listener.event.ItemRightClick
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.items.ItemInstance
import org.bukkit.entity.Arrow
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.NamespacedKey
import org.bukkit.entity.AbstractArrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import java.util.HashMap
import java.lang.NullPointerException
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.event.inventory.InventoryClickEvent

class ItemUpdate : Listener {
    private fun update(item: ItemStack?) {
        if (item == null) return
        if (hasItemID(item)) {
            val inst = getInstByItem(Book.inst, item)
                ?: throw NullPointerException("Cannot create item instance.")
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