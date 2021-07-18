package io.github.mg138.tsbook.stat.util

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.data.Stated
import io.github.mg138.tsbook.stat.data.StatMap
import io.github.mg138.tsbook.stat.type.StatType
import kotlin.math.max

object StatUtil {
    /*
    private fun filter(filter: Set<StatType>, stats: StatMap): StatMap {
        val result = StatMap()

        filter.forEach { type ->
            stats.getStat(type)?.let {
                result.putStat(type, it)
            }
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
     */

    fun sum(vararg stats: Stated): StatMap {
        val result = StatMap()

        stats.forEach {
            it.forEach { (type, stat) ->
                result.addStat(type, stat)
            }
        }

        return result
    }

    private fun percent(m: Double, k: Int) = max(m / (m + k), 0.0)

    private fun positivePercent(m: Double, k: Int) = 1 + percent(m, k)

    private fun negativePercent(m: Double, k: Int) = 1 - percent(m, k)

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat * modifier
     * - modifier < 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier = 0: ignored
     */
    fun positiveMod(
        stat: Stat,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * modifier

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * negativePercent(modifier, constant)
    }

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat * modifier
     * - modifier < 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier = 0: ignored
     */
    fun positiveMod(
        stat: Double,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Double {
        if (modifier > 0.0) return stat * modifier

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * negativePercent(modifier, constant)
    }

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier < 0: stat gets bigger, but limited at 2x
     * - modifier = 0: ignored
     */
    fun negativeMod(
        stat: Stat,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * positivePercent(modifier, constant)
    }

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier < 0: stat gets bigger, but limited at 2x
     * - modifier = 0: ignored
     */
    fun negativeMod(
        stat: Double,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Double {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * positivePercent(modifier, constant)
    }

    /**
     * Calculates the damage *specifically* for [StatType.Preset.DamageTypes.DAMAGE_TRUE]
     * @return The calculated True Damage, cannot be less than 0
     */
    fun calculateTrueDamage(damage: Stat, defense: Stat): Stat {
        return (damage - defense).ensureAtLeast(0.0)
    }

    fun damageMod(damage: Stat, modifier: Double, ignoreNegative: Boolean = false) =
        positiveMod(damage, modifier, 2, ignoreNegative)

    fun damageMod(damage: Double, modifier: Double, ignoreNegative: Boolean = false) =
        positiveMod(damage, modifier, 2, ignoreNegative)

    fun defense(damage: Stat, defense: Double, ignoreNegative: Boolean = false) =
        negativeMod(damage, defense, 100, ignoreNegative)

    fun defense(damage: Double, defense: Double, ignoreNegative: Boolean = false) =
        negativeMod(damage, defense, 100, ignoreNegative)
}