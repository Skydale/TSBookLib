package io.github.mg138.tsbook.listener.event.inventory.error

import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.Sound
import org.bukkit.entity.Player

object ItemError {
    fun badItem(player: Player) {
        player.sendMessage(BookConfig.language.errors.gui.badItem)
        player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
    }
}