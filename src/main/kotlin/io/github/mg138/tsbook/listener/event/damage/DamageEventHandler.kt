package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.listener.event.damage.DamageHandler.simpleDamage
import io.github.mg138.tsbook.listener.event.damage.DamageHandler.damagedByEntity
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import io.github.mg138.tsbook.entity.effect.data.StatusType
import org.bukkit.event.entity.EntityDamageByEntityEvent
import io.github.mg138.tsbook.attribute.stat.StatMap
import io.github.mg138.tsbook.attribute.stat.util.StatUtil
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.mob.MobConfig
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import kotlin.math.max

object DamageEventHandler : Listener {
    private val damageCD: MutableMap<Entity, Long> = HashMap()

    fun unload() {
        damageCD.clear()
        DamageHandler.unload()
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        damageCD.remove(event.entity)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        DamageHandler.remove(player)
        damageCD.remove(player)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity !is LivingEntity || entity is ArmorStand || entity is Item) return

        when (event.cause) {
            DamageCause.FIRE_TICK, DamageCause.FIRE, DamageCause.LAVA -> {
                event.isCancelled = true
                if (System.currentTimeMillis() - damageCD.getOrDefault(entity, 0L) > 500) {
                    simpleDamage(entity, 2.0)
                    damageCD[entity] = System.currentTimeMillis()
                }
                return
            }
            DamageCause.FALL -> {
                EffectHandler.getEffect(entity, StatusType.FALL_DAMAGE_RESISTANCE)?.let {
                    event.isCancelled = true
                    simpleDamage(
                        entity,
                        StatUtil.calculateModifier(
                            event.damage,
                            max((1 - it.entityStatus.status.power), 0.0)
                        )
                    )
                    return
                }
            }
            else -> Unit
        }
        if (event is EntityDamageByEntityEvent) {
            when {
                DamageHandler.mythicMobHelper.isMythicMob(entity) -> {
                    MobConfig[DamageHandler.mythicMobHelper.getMythicMobInstance(entity).type.internalName]?.let {
                        damagedByEntity(event, it.stats)
                        return
                    }
                }
                entity is Player -> {
                    val stats = StatMap()
                    ArcticGlobalDataService.inst.getData<PlayerData>(entity, PlayerData::class)
                        ?.equipment?.forEach { _, armor ->
                            armor.itemStat?.let {
                                stats.putAll(StatUtil.getDefense(it))
                            }
                        }
                    damagedByEntity(event, stats)
                    return
                }
            }
            damagedByEntity(event, StatMap())
        }
    }
}