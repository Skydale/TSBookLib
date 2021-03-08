package io.github.mg138.tsbook.listener.event.click

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.listener.event.util.ArmorType
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.players.util.ArmorUtil
import io.github.mg138.tsbook.utils.config.Config
import io.github.mg138.tsbook.utils.config.gui.ArmorGUIConfig
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class RightClickEvent(
    private val config: Config,
    private val armorConfig: ArmorGUIConfig = config.armorConfig
) : Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    fun onRightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            val item = event.item ?: return
            val instance = ItemUtils.getInstByItem(Book.inst, item) ?: return

            event.isCancelled = true
            event.setUseItemInHand(Event.Result.DENY)

            val i = ArmorUtil.getByType(instance.itemType, player, armorConfig)
            if (i != -1) {
                ArcticGlobalDataService.dataServiceInstance.edit<PlayerData>(player) {
                    it.equipment[i] = instance
                }
                if (!ArmorType.isArmor(item.type)) player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1F)
                player.inventory.setItemInMainHand(null)
            }
        }
    }
}