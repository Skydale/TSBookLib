package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.listener.event.damage.DamageHandler.simpleDamage
import io.github.mg138.tsbook.entities.effect.EffectHandler.hasEffect
import io.github.mg138.tsbook.entities.effect.EffectHandler.getEffect
import io.github.mg138.tsbook.Book.Companion.setting
import io.github.mg138.tsbook.listener.event.damage.DamageHandler.damagedByEntity
import io.github.mg138.tsbook.players.ArcticGlobalDataService.Companion.playerDataRef
import io.github.mg138.tsbook.listener.event.damage.DamageHandler.getDefense
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import org.bukkit.event.entity.EntityDamageByEntityEvent
import io.github.mg138.tsbook.items.data.stat.StatType
import io.github.mg138.tsbook.items.data.stat.util.set.DefenseType
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.players.data.PlayerData
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class DamageEventHandler : Listener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        lastDamageTime.remove(event.entity)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        DamageHandler.damageCD.remove(event.player)
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
                if (System.currentTimeMillis() - lastDamageTime.getOrDefault(entity, 0L) > 500) {
                    simpleDamage(entity, 2.0)
                    lastDamageTime[entity] = System.currentTimeMillis()
                }
                return
            }
            DamageCause.FALL -> if (hasEffect(entity, StatusEffectType.FALL_DAMAGE_RESISTANCE)) {
                event.isCancelled = true
                val effect = getEffect(entity, StatusEffectType.FALL_DAMAGE_RESISTANCE)!!
                simpleDamage(entity, event.damage * (1 - effect.power))
                return
            }
            else -> Unit
        }
        if (event is EntityDamageByEntityEvent) {
            if (mythicMobHelper.isMythicMob(entity)) {
                val mob = setting.getMMMob(mythicMobHelper.getMythicMobInstance(entity).type.internalName)
                if (mob != null) {
                    val map: MutableMap<StatType, Double> = EnumMap(StatType::class.java)
                    for (literalType in mob.getKeys(false)) {
                        val type = StatType.valueOf(literalType.toUpperCase())
                        if (DefenseType.types.contains(type)) map[type] = mob.getDouble(literalType)
                    }
                    damagedByEntity(event, map)
                    return
                }
            } else if (entity is Player) {
                val stats: MutableList<ItemStats> = ArrayList()
                val data = ArcticGlobalDataService.dataServiceInstance.getData<PlayerData>(
                    entity, PlayerData::class
                )
                data?.equipment?.forEach { _, armor -> armor.stats?.let { stats.add(it) } }
                damagedByEntity(event, getDefense(stats.toTypedArray()))
                return
            }
            damagedByEntity(event, emptyMap())
        }
    }

    companion object {
        private val mythicMobHelper = MythicMobs.inst().apiHelper
        private val lastDamageTime: MutableMap<Entity, Long> = HashMap()
        fun unload() {
            lastDamageTime.clear()
        }
    }
}