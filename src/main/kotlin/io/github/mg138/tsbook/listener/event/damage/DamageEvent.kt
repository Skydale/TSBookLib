package io.github.mg138.tsbook.listener.event.damage

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.service.reactant.service.MythicMobService
import io.github.mg138.tsbook.stat.data.StatMap
import io.github.mg138.tsbook.stat.data.Stated
import io.github.mg138.tsbook.player.data.PlayerData
import io.github.mg138.tsbook.config.mob.MobConfig
import org.bukkit.entity.*
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.*
import tech.clayclaw.arcticglobal.service.PlayerDataService
import java.util.*

// TODO bad bad :((

/*
@Component
class DamageEvent(
    private val eventService: EventService,
    private val dataService: PlayerDataService,
    mythicMobService: MythicMobService
) : LifeCycleHook {
    private val mythicHelper = mythicMobService.mythicMobHelper

    private fun Entity.attackable(): Boolean {
        if (this !is LivingEntity) return false

        return when (this) {
            is ArmorStand, is Item -> false
            else -> true
        }
    }



    override fun onEnable() {
        eventService {
            EntityDamageByEntityEvent::class.observable(EventPriority.HIGHEST)
                .filter { it.entity.attackable() }
                .subscribe { event ->
                    val entity = event.entity

                    when {
                        mythicHelper.isMythicMob(entity) -> {
                            MobConfig[DamageHandler.mythicMobHelper.getMythicMobInstance(entity).type.internalName]?.let {
                                damagedByEntity(event, it.stats)
                            }
                        }
                        entity is Player -> {
                            val stats = StatMap()
                            dataService.getData<PlayerData>(entity, PlayerData::class)
                                ?.equipment?.values?.forEach { item ->
                                    if (item is Stated) {
                                        item.forEach {
                                            stats.putStat(it)
                                        }
                                    }
                                }
                            damagedByEntity(event, stats)
                        }
                        else -> damagedByEntity(event, StatMap())
                    }
                }
        }
    }
}

 */