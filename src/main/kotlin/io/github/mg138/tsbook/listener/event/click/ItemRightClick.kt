package io.github.mg138.tsbook.listener.event.click

import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.item.interact.Clickable
import io.github.mg138.tsbook.item.api.ItemManager
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

//todo migrate
/*
class ItemRightClick(
    private val itemManager: ItemManager,
    private val eventService: EventService
) : LifeCycleHook {
    override fun onEnable() {
        eventService {
            PlayerInteractEntityEvent::class.observable().subscribe {
                onPlayerInteractEntity(it)
            }
            PlayerInteractEvent::class.observable().subscribe {
                onPlayerInteract(it)
            }
        }
    }

    private fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        val itemStack = player.inventory.itemInMainHand
        val item = itemManager.get(itemStack) ?: return

        if (item is Clickable) {
            item.onPlayerInteractEntity(event)
        }
    }

    private fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val itemStack = player.inventory.itemInMainHand
        val item = itemManager.get(itemStack) ?: return

        if (item is Clickable) {
            item.onPlayerInteract(event)
        }
    }

    /*
    private fun book(item: ItemStack, player: Player) {
        if (System.currentTimeMillis() - damageCD.getOrDefault(player, 0L) <= 500) {
            return
        }
        damageCD[player] = System.currentTimeMillis()

        player.setCooldown(item.type, 10)
        val uuids: MutableList<UUID> = ArrayList()

        getUUID(item)?.let { uuids += it }

        ArcticGlobalDataService.inst.getData<PlayerData>(player, PlayerData::class)?.equipment?.forEach { _, instance ->
            uuids += instance.uuid
        }
        val arrow = player.world.spawn(player.eyeLocation, Arrow::class.java) { aw: Arrow ->
            aw.persistentDataContainer[ItemUtil.UUID_ARRAY_KEY, UUIDArrayTag] = uuids.toTypedArray()
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
    */
}
 */