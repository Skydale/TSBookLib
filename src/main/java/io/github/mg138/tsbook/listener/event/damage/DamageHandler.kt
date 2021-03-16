package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entities.effect.EffectHandler
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import io.github.mg138.tsbook.entities.util.MobType
import io.github.mg138.tsbook.items.ItemIdentification
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.items.data.stat.StatMap
import io.github.mg138.tsbook.items.data.stat.StatSingle
import io.github.mg138.tsbook.items.data.stat.StatType
import io.github.mg138.tsbook.items.data.stat.util.set.*
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.listener.event.damage.utils.StatCalculator
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.BookSetting
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*
import kotlin.collections.HashMap

object DamageHandler {
    private val mythicMobHelper: BukkitAPIHelper = MythicMobs.inst().apiHelper
    private val rand = Random()
    private val bookSetting: BookSetting = Book.setting
    val damageCD: MutableMap<Player, Long> = HashMap()

    fun damagedByEntity(event: EntityDamageByEntityEvent, defense: Map<StatType, Double>) {
        if (event.entity !is LivingEntity) return
        val damager = event.damager
        when {
            mythicMobHelper.isMythicMob(damager) -> {
                val mobSetting = bookSetting.getMMMob(mythicMobHelper.getMythicMobInstance(damager).type.internalName) ?: return


                val fakeItemStats = arrayOf(ItemStats(identification, bookSetting, stats))
                complexDamage(event, fakeItemStats, defense)
            }
            damager is Player -> {
                damageCD.putIfAbsent(damager, System.currentTimeMillis())
                if ((System.currentTimeMillis() - damageCD[damager]!!) < 500) {
                    event.isCancelled = true
                    return
                }
                damageCD[damager] = System.currentTimeMillis()

                val stats: Stack<ItemStats> = Stack()
                val equipment = damager.equipment ?: return
                val item = equipment.itemInMainHand
                if (ItemUtils.checkItem(item) {}) {
                    ItemUtils.getInstByItem(Book.inst, item)?.stats?.let { stats.add(it) }
                }
                ArcticGlobalDataService.inst.getData<PlayerData>(damager, PlayerData::class)
                    ?.equipment?.forEach { _, armor -> armor.stats?.let { stats.add(it) } }

                complexDamage(event, stats.toTypedArray(), defense)
            }
            damager is Arrow -> {
                val container = damager.persistentDataContainer
                val uuids: Array<UUID> = container[ItemUtils.uuidArrayKey, ItemUtils.uuidArrayTag] ?: return
                val stats: MutableList<ItemStats> = ArrayList()
                for (uuid in uuids) {
                    val inst: ItemInstance = ItemUtils.itemCache[uuid] ?: continue
                    inst.stats?.let { stats.add(it) }
                }
                complexDamage(event, stats.toTypedArray(), defense)
            }
        }
    }

    fun complexDamage(event: EntityDamageByEntityEvent, stats: Array<ItemStats>?, defense: Map<StatType, Double>) {
        val livingEntity: LivingEntity = event.entity as LivingEntity
        if (stats == null) {
            simpleDamage(livingEntity, 1.0)
            return
        }
        var damage = 0.0

        val damager: Entity = event.damager
        val livingDamager: LivingEntity
        var customEvent: CustomDamageEvent? = null

        when (damager) {
            is LivingEntity -> {
                livingDamager = damager
                customEvent = CustomDamageEvent(livingEntity, livingDamager)
            }
            is Projectile -> {
                if (damager.shooter is LivingEntity) {
                    livingDamager = damager.shooter as LivingEntity
                    customEvent = CustomDamageEvent(livingEntity, livingDamager)
                }
            }
        }
        var usedModifier = 0.0
        for ((type, modifier) in getModifier(stats)) {
            when (type) {
                StatType.MODIFIER_HELL -> if (MobType.isHellish(livingEntity.type)) usedModifier += modifier / 100
                StatType.MODIFIER_MOBS -> if (MobType.isMob(livingEntity.type)) usedModifier += modifier / 100
                StatType.MODIFIER_PLAYER -> if (livingEntity.type == EntityType.PLAYER) usedModifier += modifier / 100
                StatType.MODIFIER_ARTHROPOD -> if (MobType.isArthropod(livingEntity.type)) usedModifier += modifier / 100
                StatType.MODIFIER_UNDERWATER -> if (MobType.isWatery(livingEntity.type)) usedModifier += modifier / 100
                StatType.MODIFIER_UNDEAD -> if (MobType.isUndead(livingEntity.type)) usedModifier += modifier / 100
                else -> Unit
            }
        }
        val damages: Map<StatType, Double> = getDamage(stats)
        val critDamage = getStat(EnumSet.of(StatType.POWER_CRITICAL), stats)[StatType.POWER_CRITICAL] ?: 0.0
        var critChance = getStat(EnumSet.of(StatType.CHANCE_CRITICAL), stats)[StatType.CHANCE_CRITICAL] ?: 0.0
        for (damageType in damages.keys) {
            var currentDamage: Double
            if (damageType === StatType.DAMAGE_TRUE) {
                currentDamage = damages[StatType.DAMAGE_TRUE]?.let {
                    StatCalculator.calculateTrueDamage(
                        it,
                        defense.getOrDefault(StatType.DEFENSE_TRUE, 0.0),
                        1 + usedModifier
                    )
                }!!
            } else {
                var critical = 0
                critChance /= 100.0
                while (true) {
                    val criticalSuccess = rand.nextFloat() < critChance
                    if (!criticalSuccess) break
                    critChance -= 1.0
                    critical++
                }
                currentDamage = damages[damageType]?.let {
                    StatCalculator.calculateDamage(
                        it,
                        defense.getOrDefault(DamageDefenseRelation.relationship.get(damageType), 0.0),
                        1 + (critical * (1 + critDamage / 100) - 1) + usedModifier
                    )
                }!!
            }
            damage += currentDamage
            customEvent?.addDamage(damageType, currentDamage)

            var chance = 0.25
            var x = 0
            when (damageType) {
                StatType.DAMAGE_TEMPUS, StatType.DAMAGE_AQUA,
                StatType.DAMAGE_IGNIS, StatType.DAMAGE_LUMEN,
                StatType.DAMAGE_TERRA, StatType.DAMAGE_UMBRA -> {
                    chance = 0.25 + (getStat(EnumSet.of(StatType.AFFINITY_ELEMENT), stats)[StatType.AFFINITY_ELEMENT]
                        ?: 0.0) / 100
                }
                else -> Unit
            }
            while (true) {
                val success = rand.nextFloat() < chance
                if (!success) break
                chance -= 1.0
                x++
            }
            if (x != 0) {
                when (damageType) {
                    StatType.DAMAGE_IGNIS -> {
                        val power = x * currentDamage / 8
                        if (power < 20) break
                        EffectHandler.apply(StatusEffectType.BURNING, livingEntity, power, (currentDamage / 6).toInt())
                    }
                    StatType.DAMAGE_PHYSICAL -> {
                        val power = x * currentDamage / 12
                        if (power < 20) break
                        EffectHandler.apply(
                            StatusEffectType.BLEEDING,
                            livingEntity,
                            power,
                            (currentDamage / 14).toInt()
                        )
                    }
                    StatType.DAMAGE_TEMPUS -> {
                        val power = x * currentDamage / 8
                        if (power < 20) break
                        EffectHandler.apply(
                            StatusEffectType.PARALYSIS,
                            livingEntity,
                            power,
                            (600 * (currentDamage / (currentDamage + 1000))).toInt()
                        )
                    }
                    else -> Unit
                }
            }
        }
        val effectPower: Map<StatType, Double> = getEffectPower(stats)
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
                    if (livingDamager == null) break
                    val result: Double = livingDamager.getHealth() + damage * Math.min(1.0, power / 100)
                    val maxHealth: Double =
                        Objects.requireNonNull(livingDamager.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                            .getBaseValue()
                    livingDamager.setHealth(Math.min(result, maxHealth))
                }
                StatType.CHANCE_SLOWNESS -> {
                    val power = effectPower[StatType.POWER_SLOWNESS] ?: break
                    EffectHandler.apply(
                        StatusEffectType.SLOWNESS,
                        livingEntity,
                        x * power / 100,
                        (x * power * 90).toInt()
                    )
                }
                StatType.CHANCE_LEVITATION -> {
                    val power = effectPower[StatType.POWER_LEVITATION] ?: break
                    EffectHandler.apply(StatusEffectType.LEVITATION, livingEntity, 0.0, (x * power * 20).toInt())
                }
                StatType.CHANCE_NAUSEOUS -> {
                    val power = effectPower[StatType.POWER_NAUSEOUS] ?: break
                    EffectHandler.apply(StatusEffectType.NAUSEOUS, livingEntity, 0.0, (x * power * 20).toInt())
                }
            }
        }
        event.damage = damage
        livingEntity.maximumNoDamageTicks = 0
        livingEntity.noDamageTicks = 0
        if (customEvent != null) {
            Bukkit.getPluginManager().callEvent(customEvent)
        }
    }

    private fun getStat(template: Set<StatType>, itemStats: Array<ItemStats>): Map<StatType, Double> {
        val result: MutableMap<StatType, Double> = EnumMap(StatType::class.java)
        for (itemStat in itemStats) {
            val stats = itemStat.statMap

            template.forEach { type ->
                if (stats.containsKey(type)) {
                    result[type] = itemStat[type] + result.getOrDefault(type, 0.0)
                }
            }
        }
        return result
    }

    fun getDefense(stats: Array<ItemStats>): Map<StatType, Double> {
        return getStat(DefenseType.types, stats)
    }

    fun getModifier(stats: Array<ItemStats>): Map<StatType, Double> {
        return getStat(ModifierType.types, stats)
    }

    fun getDamage(stats: Array<ItemStats>): Map<StatType, Double> {
        return getStat(DamageType.types, stats)
    }

    fun getEffectPower(stats: Array<ItemStats>): Map<StatType, Double> {
        return getStat(EffectPowerType.types, stats)
    }

    fun getEffectChance(stats: Array<ItemStats>): Map<StatType, Double> {
        return getStat(EffectChanceType.types, stats)
    }

    fun simpleDamage(
        entity: LivingEntity,
        damage: Double,
        damageType: StatType? = null,
        display: Boolean = false
    ): Boolean {
        if (entity.isDead) return false
        entity.damage(damage)
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        if (display && DamageType.types.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.location)
        }
        return true
    }
}