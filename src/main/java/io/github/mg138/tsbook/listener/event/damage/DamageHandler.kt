package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entities.effect.EffectHandler
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import io.github.mg138.tsbook.utils.MobType
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.stat.*
import io.github.mg138.tsbook.stat.util.StatUtil
import io.github.mg138.tsbook.stat.util.set.*
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.stat.util.map.DamageDefenseRelation
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*
import kotlin.collections.HashMap

object DamageHandler {
    val mythicMobHelper: BukkitAPIHelper = MythicMobs.inst().apiHelper
    private val rand = Random()
    private val damageCD: MutableMap<Player, Long> = HashMap()

    fun remove(player: Player) {
        damageCD.remove(player)
    }

    fun unload() {
        damageCD.clear()
    }

    fun damagedByEntity(event: EntityDamageByEntityEvent, defense: StatMap) {
        val damager = event.damager

        when {
            mythicMobHelper.isMythicMob(damager) -> {
                MobConfig[mythicMobHelper.getMythicMobInstance(damager).type.internalName]?.let {
                    complexDamage(event, it.itemStats.statOut, defense)
                }
            }
            damager is Player -> {
                if (System.currentTimeMillis() - damageCD.getOrDefault(damager, 0L) <= 500) {
                    event.isCancelled = true
                    return
                }
                damageCD[damager] = System.currentTimeMillis()

                val itemStats: MutableList<ItemStats> = LinkedList()
                damager.equipment?.itemInMainHand?.let { item ->
                    if (ItemUtils.checkItem(item)) {
                        ItemUtils.getInstByItem(Book.inst, item)?.stats?.let { itemStats.add(it) }
                    }
                }
                ArcticGlobalDataService.inst.getData<PlayerData>(damager, PlayerData::class)
                    ?.equipment?.forEach { _, armor -> armor.stats?.let { itemStats.add(it) } }

                complexDamage(event, StatUtil.combine(itemStats), defense)
            }
            damager is Arrow -> {
                val itemStats: MutableList<ItemStats> = LinkedList()
                damager.persistentDataContainer[ItemUtils.uuidArrayKey, ItemUtils.uuidArrayTag]
                    ?.forEach { uuid ->
                        ItemUtils.itemCache[uuid]?.let { inst ->
                            inst.stats?.let { itemStats.add(it) }
                        }
                    }
                complexDamage(event, StatUtil.combine(itemStats), defense)
            }
        }
    }

    fun complexDamage(event: EntityDamageByEntityEvent, stats: StatMap, defense: StatMap) {
        val entity = event.entity as LivingEntity
        val damager: LivingEntity

        var customEvent: CustomDamageEvent? = null

        when (val entityDamager = event.damager) {
            is LivingEntity -> {
                damager = entityDamager
                customEvent = CustomDamageEvent(entity, damager)
            }
            is Projectile -> {
                if (entityDamager.shooter is LivingEntity) {
                    damager = entityDamager.shooter as LivingEntity
                    customEvent = CustomDamageEvent(entity, damager)
                }
            }
        }

        var usedModifier = 0.0
        StatUtil.getModifier(stats).forEach { (type, modifier) ->
            when (type) {
                StatType.MODIFIER_HELL -> if (MobType.isHellish(entity.type)) usedModifier += modifier.stat / 100
                StatType.MODIFIER_MOBS -> if (MobType.isMob(entity.type)) usedModifier += modifier.stat / 100
                StatType.MODIFIER_PLAYER -> if (entity.type == EntityType.PLAYER) usedModifier += modifier.stat / 100
                StatType.MODIFIER_ARTHROPOD -> if (MobType.isArthropod(entity.type)) usedModifier += modifier.stat / 100
                StatType.MODIFIER_UNDERWATER -> if (MobType.isWatery(entity.type)) usedModifier += modifier.stat / 100
                StatType.MODIFIER_UNDEAD -> if (MobType.isUndead(entity.type)) usedModifier += modifier.stat / 100
                else -> Unit
            }
        }

        val damageSum = damage(stats, defense, usedModifier)
        elementEffect(stats, entity, usedModifier)
        val effectPower: StatMap = getEffectPower(stats)
        for (entry in getEffectChance(stats).entries) {
            val type: StatType = entry.key
            var chance = entry.value
            var x = 0
            chance /= 100.0
            while (true) {
                val success = rand.nextFloat() < chance
                if (!success) break
                chance -= 1.0
                x++
            }
            if (x == 0) continue
            when (type) {
                StatType.CHANCE_DRAIN -> {
                    val power = effectPower[StatType.POWER_DRAIN] ?: break
                    if (damager == null) break
                    val result: Double = damager.getHealth() + damageSum * Math.min(1.0, power / 100)
                    val maxHealth: Double =
                        Objects.requireNonNull(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                            .getBaseValue()
                    damager.setHealth(Math.min(result, maxHealth))
                }
                StatType.CHANCE_SLOWNESS -> {
                    val power = effectPower[StatType.POWER_SLOWNESS] ?: break
                    EffectHandler.apply(
                        StatusEffectType.SLOWNESS,
                        entity,
                        x * power / 100,
                        (x * power * 90).toInt()
                    )
                }
                StatType.CHANCE_LEVITATION -> {
                    val power = effectPower[StatType.POWER_LEVITATION] ?: break
                    EffectHandler.apply(StatusEffectType.LEVITATION, entity, 0.0, (x * power * 20).toInt())
                }
                StatType.CHANCE_NAUSEOUS -> {
                    val power = effectPower[StatType.POWER_NAUSEOUS] ?: break
                    EffectHandler.apply(StatusEffectType.NAUSEOUS, entity, 0.0, (x * power * 20).toInt())
                }
            }
        }
        event.damage = damageSum
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        if (customEvent != null) {
            Bukkit.getPluginManager().callEvent(customEvent)
        }
    }

    fun simpleDamage(
        entity: LivingEntity,
        damage: Double,
        damageType: StatType = StatType.DAMAGE_NONE,
        display: Boolean = false
    ): Boolean {
        if (entity.isDead) return false
        if (damage == 0.0) return true
        entity.damage(damage)
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        if (display && DamageType.types.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.location)
        }
        return true
    }

    private fun damage(stats: StatMap, defense: StatMap, modifier: Double): Double {
        val damages = StatUtil.getDamage(stats)
        if (damages.isEmpty()) return 0.0

        var certainCritStrikes = 0
        val critDamage = StatUtil.checkBelowZero(
            stat = stats.getStatOut(StatType.POWER_CRITICAL),
            above = { it / 100 }
        )
        val critChance = StatUtil.checkBelowZero(
            stat = stats.getStatOut(StatType.CHANCE_CRITICAL),
            above = {
                val temp = it / 100
                certainCritStrikes = temp.toInt()
                temp % 1
            }
        )

        println(critChance)
        println(critDamage)

        var damageSum = 0.0
        damages.forEach { (damageType, rawDamage) ->
            val critStrikes = certainCritStrikes + (if (rand.nextDouble() < critChance) 1 else 0)
            val damage = when (damageType) {
                StatType.DAMAGE_TRUE -> {
                    StatUtil.calculateTrueDamage(
                        damage = rawDamage.stat,
                        defense = defense.getStatOut(StatType.DEFENSE_TRUE),
                        modifier = 1 + modifier
                    )
                }
                else -> {
                    StatUtil.calculateDamage(
                        damage = rawDamage.stat,
                        defense = defense.getStatOut(DamageDefenseRelation.relation[damageType]!!),
                        modifier = 1 + modifier + (critDamage * critStrikes)
                    )
                }
            }
            damageSum += damage
        }
        return damageSum
    }

    private fun elementEffect(stats: StatMap, entity: LivingEntity, modifier: Double) {
        val elementDamages = StatUtil.getElementDamage(stats)
        if (elementDamages.isEmpty()) return

        var certainStrike = 0
        val chance = StatUtil.checkBelowZero(
            stat = (0.25 + stats.getStatOut(StatType.AFFINITY_ELEMENT)),
            above = {
                val temp = it / 100
                certainStrike = temp.toInt()
                temp % 1
            }
        )

        elementDamages.forEach { (damageType, rawDamage) ->
            val damage = StatUtil.calculateModifier(rawDamage.stat, modifier)
            val strikes = (certainStrike + if (rand.nextDouble() < chance) 1 else 0)
            when (damageType) {
                StatType.DAMAGE_IGNIS -> {
                    val power = (damage / 8) * strikes
                    val tick = (damage / 6).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.apply(StatusEffectType.BURNING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_PHYSICAL -> {
                    val power = (damage / 12) * strikes
                    val tick = (damage / 14).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.apply(StatusEffectType.BLEEDING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_TEMPUS -> {
                    val power = (damage / 8) * strikes
                    val tick = (600 * (damage / (damage + 1000))).toLong()
                    if (power > 20 && tick > 60) {
                        EffectHandler.apply(StatusEffectType.PARALYSIS, entity, power, tick)
                    }
                }
                else -> Unit
            }
        }
    }
}