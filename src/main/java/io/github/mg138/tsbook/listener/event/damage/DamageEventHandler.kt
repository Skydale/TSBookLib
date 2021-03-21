package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.listener.event.damage.DamageHandler.simpleDamage
import io.github.mg138.tsbook.entities.effect.EffectHandler.hasEffect
import io.github.mg138.tsbook.entities.effect.EffectHandler.getEffect
import io.github.mg138.tsbook.listener.event.damage.DamageHandler.damagedByEntity
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import org.bukkit.event.entity.EntityDamageByEntityEvent
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.stat.StatMap
import io.github.mg138.tsbook.stat.util.StatUtil
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.stat.util.set.DefenseType
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import kotlin.math.max

class DamageEventHandler : Listener {
    companion object {
        private val damageCD: MutableMap<Entity, Long> = HashMap()

        fun unload() {
            damageCD.clear()
            DamageHandler.unload()
        }
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

        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0

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
                getEffect(entity, StatusEffectType.FALL_DAMAGE_RESISTANCE)?.let {
                    event.isCancelled = true
                    simpleDamage(entity, StatUtil.calculateModifier(event.damage, max((1 - it.power), 0.0)))
                    return
                }
            }
            else -> Unit
        }
        if (event is EntityDamageByEntityEvent) {
            when {
                DamageHandler.mythicMobHelper.isMythicMob(entity) -> {
                    MobConfig[DamageHandler.mythicMobHelper.getMythicMobInstance(entity).type.internalName]?.let {
                        damagedByEntity(event, StatUtil.getDefense(it.itemStats.statOut))
                        return
                    }
                }
                entity is Player -> {
                    val stats = StatMap()
                    ArcticGlobalDataService.inst.getData<PlayerData>(entity, PlayerData::class)
                        ?.equipment?.forEach { _, armor ->
                            armor.stats?.let { itemStat ->
                                itemStat.statOut.forEach { (type, stat) ->
                                    if (DefenseType.types.contains(type)) {
                                        stats[type] = stat + stats[type]
                                    }
                                }
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