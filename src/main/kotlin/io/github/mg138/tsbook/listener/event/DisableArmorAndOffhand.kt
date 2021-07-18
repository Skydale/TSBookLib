package io.github.mg138.tsbook.listener.event

import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.material.util.MaterialUtil
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent

// todo rewrite

/*
class DisableArmorAndOffhand(
    private val eventService: EventService
) : LifeCycleHook {
    override fun onEnable() {
        eventService {
            PlayerInteractEvent::class.observable().subscribe {
                disableArmorUse(it)
            }

            InventoryClickEvent::class.observable(EventPriority.LOWEST).subscribe {
                onPlayerInventoryClick(it)
            }

            InventoryDragEvent::class.observable().subscribe {
                onPlayerInventoryDrag(it)
            }
        }
    }

    private fun isDisabledSlot(slot: Int) = (slot in 5..8 || slot == 45)

    private fun hasDisabledSlot(slots: Iterable<Int>): Boolean {
        slots.forEach {
            if (isDisabledSlot(it)) return true
        }
        return false
    }

    private fun disabledSlots(event: Cancellable, slots: Iterable<Int>) {
        if (hasDisabledSlot(slots)) {
            event.isCancelled = true
        }
    }

    private fun disabledSlot(event: Cancellable, slot: Int) {
        if (isDisabledSlot(slot)) {
            event.isCancelled = true
        }
    }

    private fun disableCrafting(event: Cancellable, type: InventoryType) {
        if (type == InventoryType.CRAFTING) {
            event.isCancelled = true
        }
    }

    private fun disablePlayerSlots(event: Cancellable, type: InventoryType, slots: Iterable<Int>) {
        if (type == InventoryType.PLAYER) {
            this.disabledSlots(event, slots)
        }
    }

    private fun disablePlayerSlots(event: Cancellable, type: InventoryType, slot: Int) {
        if (type == InventoryType.PLAYER) {
            this.disabledSlot(event, slot)
        }
    }

    private fun disableArmorShift(event: InventoryClickEvent) {
        if (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            val item = event.currentItem ?: return

            if (MaterialUtil.isArmor(item.type)) event.isCancelled = true
        }
    }

    private fun onPlayerInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        val type = event.inventory.type
        val slots = event.rawSlots

        this.disableCrafting(event, type)
        this.disablePlayerSlots(event, type, slots)

        player.updateInventory()
    }

    private fun onPlayerInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val type = event.inventory.type
        val slot = event.rawSlot

        this.disableCrafting(event, type)
        this.disabledSlot(event, slot)
        this.disableArmorShift(event)

        player.updateInventory()
    }


    private fun disableArmorUse(event: PlayerInteractEvent) {
        val item = event.item ?: return

        if (MaterialUtil.isArmor(item.type)) {
            event.isCancelled = true
            event.setUseItemInHand(Event.Result.DENY)
        }
    }
}
 */