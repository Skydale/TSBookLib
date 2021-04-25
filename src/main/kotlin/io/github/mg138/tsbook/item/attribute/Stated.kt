package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.StatType

interface Stated {
    fun getStatOut(type: StatType): Double
    fun getStat(type: StatType): Stat?
}