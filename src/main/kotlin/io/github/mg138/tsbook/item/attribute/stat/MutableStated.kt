package io.github.mg138.tsbook.item.attribute.stat

import io.github.mg138.tsbook.item.attribute.stat.data.Stat
import io.github.mg138.tsbook.item.attribute.stat.data.StatType

interface MutableStated : Stated {
    fun putStat(type: StatType, stat: Stat): Stat?
    fun remove(type: StatType): Stat?

    /**
     * Puts all the elements in the [stats] into it, overriding the previous value.
     *
     * Not to be confused with [addAll], which adds the elements in [stats] onto the pre-existing ones
     */
    fun putAll(stats: Stated)

    /**
     * Adds the elements in [stats] onto the pre-existing ones, and does not override the previous value.
     *
     * Not to be confused with [putAll], which overrides the previous value.
     */
    fun addAll(stats: Stated)
}