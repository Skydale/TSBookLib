package io.github.mg138.tsbook.stat.data

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.type.StatType

interface MutableStated : Stated {
    /**
     * Adds the stat onto pre-existing value, or put it if there wasn't one.
     * @param type the type of the stat
     * @param stat the stat to add
     *
     * @return The result of adding
     */
    fun addStat(type: StatType, stat: Stat): Stat

    /**
     * Puts the stat into it.
     * @param [type] the type of the stat
     * @param [stat] the stat put in
     *
     * @return The old stat, null if none
     */
    fun putStat(type: StatType, stat: Stat): Stat?

    fun putStat(stat: Pair<StatType, Stat>): Stat?

    /**
     * Removes the [Stat] of [type] from it
     *
     * @return The old stat, null if none
     */
    fun remove(type: StatType): Stat?
}