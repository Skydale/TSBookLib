package io.github.mg138.tsbook.listener.event

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.items.ItemUtils.getStringTag
import io.github.mg138.tsbook.items.ItemUtils.getUUID
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.ArcticGlobalDataService.Companion.playerDataRef
import io.github.mg138.tsbook.players.data.PlayerData
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ItemRightClick : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityClick(event: PlayerInteractEntityEvent): Boolean {
        val player = event.player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) return false
        val id = getStringTag(item, "item")
        id?.let { itemIDOperator(it, event) }
        val type = getStringTag(item, "type")
        type?.let { itemTypeOperator(it, item, player) }
        return true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemClick(event: PlayerInteractEvent): Boolean {
        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            val item = event.item ?: return false
            if (item.type == Material.AIR) return false
            val type = getStringTag(item, "type") ?: return false
            itemTypeOperator(type, item, event.player)
            return true
        }
        return false
    }

    private fun itemTypeOperator(type: String?, item: ItemStack, player: Player) {
        when (type) {
            "BOOK" -> BOOK(item, player)
        }
    }

    private fun itemIDOperator(itemID: String?, event: PlayerInteractEntityEvent) {
        when (itemID) {
            "LASSO" -> LASSO(event)
        }
    }

    private fun LASSO(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        entity.addPassenger(event.player)
    }

    private fun BOOK(item: ItemStack, player: Player) {
        if (damageCD.putIfAbsent(player, System.currentTimeMillis()) != null) {
            if (System.currentTimeMillis() - damageCD[player]!! < 500) {
                return
            }
            damageCD.replace(player, System.currentTimeMillis())
        }
        player.setCooldown(item.type, 10)
        val uuids: MutableList<UUID> = ArrayList()
        val uuid = getUUID(item) ?: return
        uuids.add(uuid)
        val data = ArcticGlobalDataService.inst.getData<PlayerData>(player, playerDataRef)
        data?.equipment?.forEach { i: Int?, instance: ItemInstance -> uuids.add(instance.uuid) }
        val loc = player.location
        val arrow = player.world.spawn(player.eyeLocation, Arrow::class.java) { aw: Arrow ->
            val container = aw.persistentDataContainer
            container.set(
                NamespacedKey(Book.inst, "item_uuids"),
                ItemUtils.uuidArrayTag,
                uuids.toTypedArray()
            )
            aw.setGravity(false)
            aw.isInvulnerable = true
            aw.isSilent = true
            aw.shooter = player
            aw.damage = 0.0
            aw.velocity = loc.direction
            aw.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
        }
        val runnable: BukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                arrow.remove()
            }
        }
        removing[arrow] = runnable
        runnable.runTaskLater(Book.inst, 120)
    }

    companion object {
        private val removing: MutableMap<Entity, BukkitRunnable> = HashMap()
        private val damageCD: MutableMap<Player, Long> = HashMap()
        fun unload() {
            removing.forEach { (_, runnable) -> runnable.run() }
            removing.clear()
            damageCD.clear()
        }
    }
}