package io.github.mg138.tsbook.players

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.Bukkit

import com.comphenix.protocol.events.PacketEvent

import com.comphenix.protocol.PacketType

import com.comphenix.protocol.events.ListenerPriority

import com.comphenix.protocol.events.PacketAdapter
import io.github.mg138.tsbook.Book


@Component
class FreezePlayer(
    private val eventService: EventService
): LifeCycleHook {
    private val players: MutableSet<Player> = HashSet()
    private val packets: MutableMap<Player, PacketContainer> = HashMap()

    fun remove(player: Player) {
        players.remove(player)
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packets[player]!!)
        packets.remove(player)
    }

    override fun onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(Book.inst, ListenerPriority.HIGHEST, PacketType.Play.Server.POSITION) {
                override fun onPacketSending(event: PacketEvent) {
                    val player = event.player
                    if (players.contains(player)) {
                        event.isCancelled = true
                        packets[player] = event.packet
                    }
                }
            }
        )

        eventService.registerBy(this) {
            PlayerLoginEvent::class.observable()
                .subscribe { players.add(it.player) }

            PlayerQuitEvent::class.observable()
                .subscribe { players.remove(it.player) }

            PlayerCommandPreprocessEvent::class.observable()
                .filter { players.contains(it.player) }
                .subscribe { it.isCancelled = true }

            PlayerInteractEvent::class.observable()
                .filter { players.contains(it.player) }
                .subscribe { it.isCancelled = true }

            InventoryOpenEvent::class.observable()
                .filter { players.contains(it.player) }
                .subscribe { it.isCancelled = true }

            PlayerDropItemEvent::class.observable()
                .filter { players.contains(it.player) }
                .subscribe { it.isCancelled = true }

            InventoryInteractEvent::class.observable()
                .filter { players.contains(it.whoClicked as Player) }
                .subscribe { it.isCancelled = true }

            EntityDamageEvent::class.observable()
                .filter { it.entity is Player && players.contains(it.entity as Player) }
                .subscribe { it.isCancelled = true }

            EntityDamageByEntityEvent::class.observable()
                .filter { it.damager is Player && players.contains(it.damager as Player) }
                .subscribe { it.isCancelled = true }

            EntityPickupItemEvent::class.observable()
                .filter { it.entityType == EntityType.PLAYER && players.contains(it.entity as Player) }
                .subscribe { it.isCancelled = true }
        }
    }
}