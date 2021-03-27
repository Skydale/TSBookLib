package io.github.mg138.tsbook.stat.util

import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.stat.StatMap
import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.stat.util.set.*
import kotlin.math.abs

object StatUtil {
    private fun filter(template: Set<StatType>, stats: StatMap): StatMap {
        val result = StatMap()
        template.forEach { type ->
            @Suppress("DEPRECATION")
            stats[type]?.let { result[type] = it }
        }
        return result
    }

    fun getElementDamage(stats: StatMap): StatMap {
        return filter(ElementDamageType.types, stats)
    }

    fun getDefense(stats: StatMap): StatMap {
        return filter(DefenseType.types, stats)
    }

    fun getModifier(stats: StatMap): StatMap {
        return filter(ModifierType.types, stats)
    }

    fun getDamage(stats: StatMap): StatMap {
        return filter(DamageType.types, stats)
    }

    fun getEffectPower(stats: StatMap): StatMap {
        return filter(EffectPowerType.types, stats)
    }

    fun getEffectChance(stats: StatMap): StatMap {
        return filter(EffectChanceType.types, stats)
    }

    fun combine(itemStats: List<ItemStats>): StatMap {
        val result = StatMap()
        itemStats.forEach { itemStat ->
            itemStat.stats.forEach { (type, stat) ->
                result[type] = stat + result[type]
            }
            /*
             *  Since plus accepts nulls, and will ignore nulls and return the stat as it is,
             *  and a Map returns null if there's no Value to the Key,
             *  this is a way to simplify the code.
             *
             *  !!! Flipping the operands will ***NOT WORK*** !!!
             *   ^ or simply put,
             *   ^ "result[type] + stat"
             *   ^ won't work.
             */
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

    inline fun doubleCheckBelowZero(stat: Double, below: (Double) -> Double = { 0.0 }, above: (Double) -> Double = { it }): Double {
        return when {
            stat <= 0 -> below(stat)
            else -> above(stat)
        }
    }

    inline fun intCheckBelowZero(stat: Double, below: (Double) -> Int = { 0 }, above: (Double) -> Int = { it.toInt() }): Int {
        return when {
            stat <= 0 -> below(stat)
            else -> above(stat)
        }
    }
}