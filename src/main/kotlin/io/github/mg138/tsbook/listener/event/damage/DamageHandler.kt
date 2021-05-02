package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.command.admin.DebugMode
import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.StatusType
import io.github.mg138.tsbook.item.attribute.stat.data.StatMap
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.item.attribute.stat.util.StatTables
import io.github.mg138.tsbook.item.attribute.stat.util.StatTypes
import io.github.mg138.tsbook.item.attribute.stat.util.StatUtil
import io.github.mg138.tsbook.item.attribute.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.item.data.UUIDArrayTag
import io.github.mg138.tsbook.item.util.ItemUtil
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.util.MobTypeUtil
import io.github.mg138.tsbook.util.RGBUtil.toChatColor
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*
import kotlin.math.min

//TODO make it prettier :/
object DamageHandler {
    val mythicMobHelper: BukkitAPIHelper = MythicMobs.inst().apiHelper
    private val rand = Random()
    private val damageCD: MutableMap<Player, Long> = HashMap()

    private fun debug(player: Player, message: String) {
        if (DebugMode.hasOption(player, DebugMode.DebugOption.ON_DAMAGE)) {
            player.sendMessage(message)
        }
    }

    fun remove(player: Player) {
        damageCD.remove(player)
    }

    fun unload() {
        damageCD.clear()
    }

    fun  damagedByEntity(event: EntityDamageByEntityEvent, defense: StatMap) {
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

                val identifiedStats: MutableList<IdentifiedStat> = LinkedList()
                damager.equipment?.itemInMainHand?.let { item ->
                    //ItemUtil.getInstByItem(item)?.itemStat?.let { identifiedStats.add(it) }
                }
                ArcticGlobalDataService.inst.getData<PlayerData>(damager, PlayerData::class)
                    //?.equipment?.forEach { _, armor -> armor.itemStat?.let { identifiedStats.add(it) } }

                complexDamage(event, StatUtil.combine(identifiedStats), defense)
            }
            damager is Arrow -> {
                val identifiedStats: MutableList<IdentifiedStat> = LinkedList()
                damager.persistentDataContainer[ItemUtil.UUID_ARRAY_KEY, UUIDArrayTag]
                    ?.forEach { uuid ->
                        ItemUtil.ITEM_CACHE[uuid]?.let { inst ->
                            //inst.itemStat?.let { identifiedStats.add(it) }
                        }
                    }
                complexDamage(event, StatUtil.combine(identifiedStats), defense)
            }
        }
    }

    fun  complexDamage(event: EntityDamageByEntityEvent, stats: StatMap, defense: StatMap) {
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
                StatType.MODIFIER_HELL -> if (MobTypeUtil.isHellish(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_MOBS -> if (MobTypeUtil.isMob(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_PLAYER -> if (entity.type == EntityType.PLAYER) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_ARTHROPOD -> if (MobTypeUtil.isArthropod(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_UNDERWATER -> if (MobTypeUtil.isWatery(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_UNDEAD -> if (MobTypeUtil.isUndead(entity.type)) usedModifier += modifier.getStat() / 100
                else -> Unit
            }
        }

        if (damager is Player) {
            debug(
                damager, "&eModifier: &f$usedModifier\n".toChatColor()
            )
        }

        val damageSum = damage(damager, stats, defense, usedModifier, customEvent)
        elementalEffect(damager, entity, stats, usedModifier)
        effect(damager, entity, stats, damageSum)

        if (damager is Player) {
            debug(
                damager,
                ("&cDamageSum: &f$damageSum" +
                        "\n  &cDamages: &f${customEvent.getDamages()}").toChatColor()
            )
        }

        event.damage = damageSum
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        Bukkit.getPluginManager().callEvent(customEvent)
    }

    private fun roll(chance: Double): Boolean = rand.nextDouble() < chance

    private fun roundChance(chance: Double): Double = chance % 1

    private fun getStrikes(chance: Double): Int {
        return when {
            chance < 1 -> 0
            else -> chance.toInt()
        }
    }

    private fun  damage(damager: LivingEntity, stats: StatMap, defense: StatMap, modifier: Double, event: CustomDamageEvent): Double {
        val damages = StatUtil.getDamage(stats)
        if (damages.isEmpty()) return 0.0

        val critDamage = stats.getStatOut(StatType.POWER_CRITICAL).div(100)
        val critChance: Double
        val certainStrikes: Int
        stats.getStatOut(StatType.CHANCE_CRITICAL).div(100).let {
            critChance = roundChance(it)
            certainStrikes = getStrikes(it)
        }

        if (damager is Player) {
            debug(
                damager,
                "&9CritChance: &f$critChance &7/ &cCritDamage: &f$critDamage &7/ &aCertainCritStrikes: &f$certainStrikes\n".toChatColor()
            )
        }

        var damageSum = 0.0
        damages.forEach { (damageType, rawDamage) ->
            val damage = when (damageType) {
                StatType.DAMAGE_TRUE -> {
                    StatUtil.calculateTrueDamage(
                        damage = rawDamage.getStat(),
                        defense = defense.getStatOut(StatType.DEFENSE_TRUE),
                        modifier = 1 + modifier
                    )
                }
                else -> {
                    StatUtil.calculateDamage(
                        damage = rawDamage.getStat(),
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

    private fun   effect(damager: LivingEntity, entity: LivingEntity, stats: StatMap, damageSum: Double) {
        val effectChance = StatUtil.getEffectChance(stats)
        if (effectChance.isEmpty()) return

        val effectPower = StatUtil.getEffectPower(stats)

        effectChance.forEach { (type, rawChance) ->
            val chance: Double
            val strikes: Int
            rawChance.getStat().div(100).let {
                chance = roundChance(it)
                strikes = getStrikes(it) + if (roll(chance)) 1 else 0
            }

            if (damager is Player) {
                debug(
                    damager,
                    "&b$type: &7{ &9EffectChance: &f$chance &7/ &aCertainEffectStrikes: &f$strikes &7}\n".toChatColor()
                )
            }

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
                        EffectHandler.apply(
                            StatusType.SLOWNESS,
                            entity,
                            strikes * rawPower / 100,
                            (strikes * rawPower * 90).toLong()
                        )
                    }
                    StatType.CHANCE_LEVITATION -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectHandler.apply(StatusType.LEVITATION, entity, 0.0, ticks.toLong())
                    }
                    StatType.CHANCE_NAUSEOUS -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectHandler.apply(StatusType.NAUSEOUS, entity, 0.0, ticks.toLong())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun   elementalEffect(damager: LivingEntity, entity: LivingEntity, stats: StatMap, modifier: Double) {
        val elementalDamages = StatUtil.getElementalDamage(stats)
        if (elementalDamages.isEmpty()) return

        var certainStrike: Int
        val chance: Double
        (25 + stats.getStatOut(StatType.AFFINITY_ELEMENT)).div(100).let {
            chance = roundChance(it)
            certainStrike = getStrikes(it)
        }

        elementalDamages.forEach { (type, rawDamage) ->
            val damage = StatUtil.calculateModifier(rawDamage.getStat(), modifier)
            val strikes = (certainStrike + if (roll(chance)) 1 else 0)

            if (damager is Player) {
                debug(
                    damager,
                    "&b$type: &7{ &cElementDamage: &f$damage &7/ &9ElementChance: &f$chance &7/ &aCertainElementStrikes: &f$strikes &7}\n".toChatColor()
                )
            }

            when (type) {
                StatType.DAMAGE_IGNIS -> {
                    val power = (damage / 8) * strikes
                    val tick = (damage / 6).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.apply(StatusType.BURNING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_PHYSICAL -> {
                    val power = (damage / 12) * strikes
                    val tick = (damage / 14).toLong()
                    if (power > 20 && tick > 10) {
                        EffectHandler.apply(StatusType.BLEEDING, entity, power, tick)
                    }
                }
                StatType.DAMAGE_TEMPUS -> {
                    val power = (damage / 8) * strikes
                    val tick = (600 * (damage / (damage + 1000))).toLong()
                    if (power > 20 && tick > 60) {
                        EffectHandler.apply(StatusType.PARALYSIS, entity, power, tick)
                    }
                }
                else -> Unit
            }
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

        if (display && StatTypes.damages.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.location)
        }
        return true
    }
}