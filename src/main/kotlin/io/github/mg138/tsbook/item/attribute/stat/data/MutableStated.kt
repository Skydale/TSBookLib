package io.github.mg138.tsbook.item.attribute.stat.data

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.data.StatType

interface MutableStated : Stated {
    fun putStat(type: StatType, stat: Stat): Stat?
    fun remove(type: StatType): Stat?
}