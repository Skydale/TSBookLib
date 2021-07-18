package io.github.mg138.tsbook.stat.data

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.type.StatType

interface Stated : Iterable<Pair<StatType, Stat>> {
    fun types(): Set<StatType>
    fun stats(): Collection<Stat>
    fun getStatResult(type: StatType): Double
    fun getStat(type: StatType): Stat?
}