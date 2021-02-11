package io.github.mg138.tsbook.players

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
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

@Component
class FreezePlayer(
    private val eventService: EventService
): LifeCycleHook {
    val frozen: HashSet<Player> = HashSet()

    override fun onEnable() {
        eventService.registerBy(this) {
            PlayerMoveEvent::class.observable()
                .filter { frozen.contains(it.player) }
                .subscribe { it.isCancelled = true }

            PlayerCommandPreprocessEvent::class.observable()
                .filter { frozen.contains(it.player) }
                .subscribe { it.isCancelled = true }

            PlayerInteractEvent::class.observable()
                .filter { frozen.contains(it.player) }
                .subscribe { it.isCancelled = true }

            InventoryOpenEvent::class.observable()
                .filter { frozen.contains(it.player) }
                .subscribe { it.isCancelled = true }

            PlayerDropItemEvent::class.observable()
                .filter { frozen.contains(it.player) }
                .subscribe { it.isCancelled = true }

            InventoryInteractEvent::class.observable()
                .filter { frozen.contains(it.whoClicked as Player) }
                .subscribe { it.isCancelled = true }

            EntityDamageEvent::class.observable()
                .filter {it.entity is Player && frozen.contains(it.entity as Player) }
                .subscribe { it.isCancelled = true }

            EntityDamageByEntityEvent::class.observable()
                .filter {it.damager is Player && frozen.contains(it.damager as Player) }
                .subscribe { it.isCancelled = true }

            EntityPickupItemEvent::class.observable()
                .filter {it.entityType == EntityType.PLAYER && frozen.contains(it.entity as Player) }
                .subscribe { it.isCancelled = true }
        }
    }
}