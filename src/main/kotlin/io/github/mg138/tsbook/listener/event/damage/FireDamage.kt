package io.github.mg138.tsbook.listener.event.damage

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.util.TimerMap
import org.bukkit.entity.Entity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.*
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

/*
@Component
class FireDamage(
    private val eventService: EventService
) : LifeCycleHook {
    private val cooldown = TimerMap<Entity>()

    private fun DamageCause.isFireDamage(): Boolean {
        return when (this) {
            DamageCause.FIRE_TICK, DamageCause.FIRE, DamageCause.LAVA -> true
            else -> false
        }
    }

    override fun onEnable() {
        eventService {
            EntityDamageEvent::class.observable()
                .filter { it.cause.isFireDamage() }
                .subscribe { event ->
                    event.isCancelled = true

                    cooldown(event.entity, 500) {
                        DamageHandler.simpleDamage(it, 2.0)
                    }
                }

            EntityDeathEvent::class.observable()
                .subscribe { cooldown.remove(it.entity) }

            PlayerQuitEvent::class.observable()
                .subscribe { cooldown.remove(it.player) }
        }
    }

    override fun onDisable() {
        cooldown.clear()
    }
}

 */