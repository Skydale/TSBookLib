package io.github.mg138.tsbook.listener.event.click

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.item.api.ItemManager
import io.github.mg138.tsbook.player.data.PlayerData
import io.github.mg138.tsbook.config.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.material.util.MaterialUtil
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import tech.clayclaw.arcticglobal.service.PlayerDataService

// todo migrate
/*
@Component
class ArmorAutoEquip(
    private val itemManager: ItemManager,
    private val dataService: PlayerDataService,
    private val eventService: EventService
) : LifeCycleHook {
    override fun onEnable() {
        eventService {
            PlayerInteractEvent::class.observable(EventPriority.LOWEST).subscribe {
                onRightClick(it)
            }
        }
    }

    private fun onRightClick(event: PlayerInteractEvent) {
        when (event.action) {
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                val player = event.player
                val itemStack = event.item ?: return
                val item = itemManager.get(itemStack) ?: return

                event.isCancelled = true
                event.setUseItemInHand(Event.Result.DENY)

                equipItemInHand(player, item)
            }
            else -> Unit
        }
    }

    private fun equipItemInHand(player: Player, item: ItemBase) {
        val i = ArmorGUIConfig.getByType(item.getItemType(), player)

        if (i != -1) {
            dataService.edit<PlayerData>(player) {
                it.equipment[i] = item
            }

            player.inventory.setItemInMainHand(null)

            if (!MaterialUtil.isArmor(item.getMaterial())) {
                player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1F)
            }
        }
    }
}
*/