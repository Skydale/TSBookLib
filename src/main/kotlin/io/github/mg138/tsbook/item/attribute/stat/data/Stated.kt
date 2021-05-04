package io.github.mg138.tsbook.item.attribute.stat.data

import io.github.mg138.tsbook.item.attribute.stat.Stat

interface Stated : Iterable<Pair<StatType, Stat>> {
    fun types(): Set<StatType>
    fun stats(): Collection<Stat>
    fun getStatOut(type: StatType): Double
    fun getStat(type: StatType): Stat?
}