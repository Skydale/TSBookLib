package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.StatusType
import io.github.mg138.tsbook.item.ItemStat
import io.github.mg138.tsbook.item.ItemUtils
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.stat.StatMap
import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.stat.util.StatTables
import io.github.mg138.tsbook.stat.util.StatTypes
import io.github.mg138.tsbook.stat.util.StatUtil
import io.github.mg138.tsbook.util.MobType
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min

//TODO make it prettier :/
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
                    complexDamage(event, it.stats, defense)
                }
            }
            damager is Player -> {
                if (System.currentTimeMillis() - damageCD.getOrDefault(damager, 0L) <= 500) {
                    event.isCancelled = true
                    return
                }
                damageCD[damager] = System.currentTimeMillis()

                val itemStats: MutableList<ItemStat> = LinkedList()
                damager.equipment?.itemInMainHand?.let { item ->
                    ItemUtils.getInstByItem(Book.inst, item)?.itemStat?.let { itemStats.add(it) }
                }
                ArcticGlobalDataService.inst.getData<PlayerData>(damager, PlayerData::class)
                    ?.equipment?.forEach { _, armor -> armor.itemStat?.let { itemStats.add(it) } }

                complexDamage(event, StatUtil.combine(itemStats), defense)
            }
            damager is Arrow -> {
                val itemStats: MutableList<ItemStat> = LinkedList()
                damager.persistentDataContainer[ItemUtils.uuidArrayKey, ItemUtils.uuidArrayTag]
                    ?.forEach { uuid ->
                        ItemUtils.itemCache[uuid]?.let { inst ->
                            inst.itemStat?.let { itemStats.add(it) }
                        }
                    }
                complexDamage(event, StatUtil.combine(itemStats), defense)
            }
        }
    }

    fun complexDamage(event: EntityDamageByEntityEvent, stats: StatMap, defense: StatMap) {
        val entity = event.entity as LivingEntity

        val damager = when (val entityDamager = event.damager) {
            is LivingEntity -> entityDamager
            is Projectile -> entityDamager.shooter.let { it as? LivingEntity ?: return }
            else -> return
        }

        val customEvent = CustomDamageEvent(entity, damager)

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

        val damageSum = damage(stats, defense, usedModifier, customEvent)
        elementalEffect(stats, entity, usedModifier)
        effect(stats, entity, damager, damageSum)

        event.damage = damageSum
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        Bukkit.getPluginManager().callEvent(customEvent)
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

        if (display && StatTypes.damages.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.location)
        }
        return true
    }

    private fun roll(chance: Double): Boolean = rand.nextDouble() < chance

    private fun roundChance(chance: Double): Double = chance % 1

    private fun getStrikes(chance: Double): Int {
        return when {
            chance < 1 -> 0
            else -> chance.toInt()
        }
    }

    private fun damage(stats: StatMap, defense: StatMap, modifier: Double, event: CustomDamageEvent): Double {
        val damages = StatUtil.getDamage(stats)
        if (damages.isEmpty()) return 0.0

        val critDamage = stats.getStatOut(StatType.POWER_CRITICAL).div(100)
        val critChance: Double
        val certainStrikes: Int
        stats.getStatOut(StatType.CHANCE_CRITICAL).div(100).let {
            critChance = roundChance(it)
            certainStrikes = getStrikes(it)
        }

        println("crit:")
        println("    chance: $critChance")
        println("    damage: $critDamage")
        println("    certainStrikes: $certainStrikes")

        var damageSum = 0.0
        damages.forEach { (damageType, rawDamage) ->
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
                        defense = defense.getStatOut(StatTables.damageToDefense[damageType]!!),
                        modifier = 1 + modifier + (critDamage * (certainStrikes + if (roll(critChance)) 1 else 0))
                    )
                }
            }
            event.addDamage(damageType, damage)
            damageSum += damage
        }
        return damageSum
    }

    private fun effect(stats: StatMap, entity: LivingEntity, damager: LivingEntity, damageSum: Double) {
        val effectChance = StatUtil.getEffectChance(stats)
        if (effectChance.isEmpty()) return

        val effectPower = StatUtil.getEffectPower(stats)

        effectChance.forEach { (type, rawChance) ->
            val chance: Double
            val strikes: Int
            rawChance.stat.div(100).let {
                chance = roundChance(it)
                strikes = getStrikes(it) + if (roll(chance)) 1 else 0
            }

            println("effect:")
            println("    chance: $chance")
            println("    certainStrikes: $strikes")

            if (strikes > 0) {
                when (type) {
                    StatType.CHANCE_DRAIN -> {
                        val rawPower = effectPower.getStatOut(type)
                        if (rawPower > 0) {
                            val result = damager.health + damageSum * min(rawPower / 100, 0.0)
                            val maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                            damager.health = min(result, maxHealth)
                        }
                    }
                    StatType.CHANCE_SLOWNESS -> {
                        val rawPower = effectPower.getStatOut(type)
                        EffectHandler.addEffect(
                            StatusType.SLOWNESS,
                            entity,
                            strikes * rawPower / 100,
                            (strikes * rawPower * 90).toLong()
                        )
                    }
                    StatType.CHANCE_LEVITATION -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectHandler.addEffect(StatusType.LEVITATION, entity, 0.0, ticks.toLong())
                    }
                    StatType.CHANCE_NAUSEOUS -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectHandler.addEffect(StatusType.NAUSEOUS, entity, 0.0, ticks.toLong())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun elementalEffect(stats: StatMap, entity: LivingEntity, modifier: Double) {
        val elementalDamages = StatUtil.getElementalDamage(stats)
        if (elementalDamages.isEmpty()) return

        var certainStrike: Int
        val chance: Double
        (25 + stats.getStatOut(StatType.AFFINITY_ELEMENT)).div(100).let {
            chance = roundChance(it)
            certainStrike = getStrikes(it)
        }

        elementalDamages.forEach { (damageType, rawDamage) ->
            val damage = StatUtil.calculateModifier(rawDamage.stat, modifier)
            val strikes = (certainStrike + if (roll(chance)) 1 else 0)

            when (damageType) {
                StatType.DAMAGE_IGNIS -> {
                    val power = (damage / 8) * strikes
                    val tick = (damage / 6).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.addEffect(StatusType.BURNING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_PHYSICAL -> {
                    val power = (damage / 12) * strikes
                    val tick = (damage / 14).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.addEffect(StatusType.BLEEDING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_TEMPUS -> {
                    val power = (damage / 8) * strikes
                    val tick = (600 * (damage / (damage + 1000))).toLong()
                    if (power > 20 && tick > 60) {
                        EffectHandler.addEffect(StatusType.PARALYSIS, entity, power, tick)
                    }
                }
                else -> Unit
            }
        }
    }
}