package io.github.mg138.tsbook.listener.event.click

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.ItemUtils
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.util.MaterialUtil
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object ArmorAutoEquip : Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    fun onRightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            val item = event.item ?: return
            val instance = ItemUtils.getInstByItem(Book.inst, item) ?: return

            event.isCancelled = true
            event.setUseItemInHand(Event.Result.DENY)

            val i = ArmorGUIConfig.getByType(instance.itemType, player)
            if (i != -1) {
                ArcticGlobalDataService.inst.edit<PlayerData>(player) { it.equipment[i] = instance }
                player.inventory.setItemInMainHand(null)

                if (!MaterialUtil.isArmor(item.type)) player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1F)
            }
        }
    }
}