package io.github.mg138.tsbook.attribute.stat.util

import io.github.mg138.tsbook.item.ItemStat
import io.github.mg138.tsbook.attribute.stat.StatMap
import io.github.mg138.tsbook.attribute.stat.StatType
import kotlin.math.abs

object StatUtil {
    private fun filter(template: Set<StatType>, stats: StatMap): StatMap {
        val result = StatMap()
        template.forEach { type ->
            stats[type]?.let { result[type] = it }
        }
        return result
    }

    fun getElementalDamage(stats: StatMap): StatMap {
        return filter(StatTypes.elementalDamages, stats)
    }

    fun getDefense(stats: StatMap): StatMap {
        return filter(StatTypes.defenses, stats)
    }

    fun getModifier(stats: StatMap): StatMap {
        return filter(StatTypes.modifiers, stats)
    }

    fun getDamage(stats: StatMap): StatMap {
        return filter(StatTypes.damages, stats)
    }

    fun getEffectPower(stats: StatMap): StatMap {
        return filter(StatTypes.effectPowers, stats)
    }

    fun getEffectChance(stats: StatMap): StatMap {
        return filter(StatTypes.effectChances, stats)
    }

    private fun filter(template: Set<StatType>, itemStat: ItemStat): StatMap {
        val result = StatMap()
        template.forEach { type ->
            result[type] = itemStat[type]
        }
        return result
    }

    fun getElementalDamage(itemStat: ItemStat): StatMap {
        return filter(StatTypes.elementalDamages, itemStat)
    }

    fun getDefense(itemStat: ItemStat): StatMap {
        return filter(StatTypes.defenses, itemStat)
    }

    fun getModifier(itemStat: ItemStat): StatMap {
        return filter(StatTypes.modifiers, itemStat)
    }

    fun getDamage(itemStat: ItemStat): StatMap {
        return filter(StatTypes.damages, itemStat)
    }

    fun getEffectPower(itemStat: ItemStat): StatMap {
        return filter(StatTypes.effectPowers, itemStat)
    }

    fun getEffectChance(itemStat: ItemStat): StatMap {
        return filter(StatTypes.effectChances, itemStat)
    }

    fun combine(itemStats: List<ItemStat>): StatMap {
        val result = StatMap()
        itemStats.forEach { itemStat ->
            itemStat.forEach { (type, stat) -> result[type] = stat + result[type] }
        }
        return result
    }

    /**
     * Calculates the modifier
     * @param ignoreNegative ignores the modifier if it's below 0 (defaults to false) and returns 0.0
     * @return
     * - modifier > 0: stat * modifier
     * - modifier < 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier = 0: 0.0
     */
    fun calculateModifier(stat: Double, modifier: Double, ignoreNegative: Boolean = false): Double {
        val mod = abs(modifier)
        return when {
            modifier > 0.0 -> stat * mod
            modifier < 0.0 -> if (ignoreNegative) 0.0 else stat * (1 - (mod / (mod + 2)))
            else -> 0.0
        }
    }

    /**
     * Calculates the damage *specifically* for [StatType.DAMAGE_TRUE]
     * @return The calculated True Damage, cannot be less than 0
     */
    fun calculateTrueDamage(damage: Double, defense: Double, modifier: Double): Double {
        return (calculateModifier(damage, modifier) - defense).coerceAtLeast(0.0)
    }

    /**
     * Calculates the damage
     *
     * @return
     * - defense > 0: damage gets smaller, but limited at 0 (never goes under 0)
     * - defense < 0: buffs the damage up to 2x
     * - defense = 0: ignored
     */
    fun calculateDamage(damage: Double, defense: Double, modifier: Double): Double {
        val dmg = calculateModifier(damage, modifier)
        val def = abs(defense)
        return when {
            defense > 0.0 -> dmg * (1 - (def / (def + 100)))
            defense < 0.0 -> dmg * (1 + (def / (def + 100)))
            else -> dmg
        }
    }
}