package io.github.mg138.tsbook.item.attribute.stat

import io.github.mg138.tsbook.item.attribute.stat.data.Stat
import io.github.mg138.tsbook.item.attribute.stat.data.StatType

interface Stated : Iterable<Pair<StatType, Stat>> {
    fun types(): Set<StatType>
    fun stats(): Collection<Stat>
    fun getStatOut(type: StatType): Double
    fun getStat(type: StatType): Stat?
}