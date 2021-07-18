package io.github.mg138.tsbook.item.interact

import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

interface Clickable {
    fun onPlayerInteract(event: PlayerInteractEvent)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent)
}