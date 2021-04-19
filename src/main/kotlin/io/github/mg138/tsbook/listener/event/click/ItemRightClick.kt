package io.github.mg138.tsbook.listener.event.click

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.util.ItemUtil
import io.github.mg138.tsbook.item.util.ItemUtil.getStringTag
import io.github.mg138.tsbook.item.util.ItemUtil.getUUID
import io.github.mg138.tsbook.item.storage.UUIDArrayTag
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import org.bukkit.Material
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

object ItemRightClick : Listener {
    private val removing: MutableMap<Entity, BukkitRunnable> = HashMap()
    private val damageCD: MutableMap<Player, Long> = HashMap()
    fun unload() {
        removing.forEach { (_, runnable) -> runnable.run() }
        removing.clear()
        damageCD.clear()
    }

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
            if (!ItemUtil.checkItem(item)) return false
            val type = getStringTag(item, "type") ?: return false
            itemTypeOperator(type, item, event.player)
            return true
        }
        return false
    }

    private fun itemTypeOperator(type: String, item: ItemStack, player: Player) {
        when (type) {
            "BOOK" -> book(item, player)
        }
    }

    private fun itemIDOperator(itemID: String, event: PlayerInteractEntityEvent) {
        when (itemID) {
            "LASSO" -> lasso(event)
        }
    }

    private fun lasso(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        entity.addPassenger(event.player)
    }

    private fun book(item: ItemStack, player: Player) {
        if (System.currentTimeMillis() - damageCD.getOrDefault(player, 0L) <= 500) {
            return
        }
        damageCD[player] = System.currentTimeMillis()

        player.setCooldown(item.type, 10)
        val uuids: MutableList<UUID> = ArrayList()

        getUUID(item)?.let { uuids.add(it) }

        ArcticGlobalDataService.inst.getData<PlayerData>(player, PlayerData::class)?.equipment?.forEach { _, instance ->
            uuids.add(instance.uuid)
        }
        val arrow = player.world.spawn(player.eyeLocation, Arrow::class.java) { aw: Arrow ->
            aw.persistentDataContainer[ItemUtil.uuidArrayKey, UUIDArrayTag] = uuids.toTypedArray()
            aw.setGravity(false)
            aw.isInvulnerable = true
            aw.isSilent = true
            aw.shooter = player
            aw.damage = 0.0
            aw.velocity = player.location.direction
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
}