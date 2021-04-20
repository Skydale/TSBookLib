package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.StatType

interface MutableStated : Stated {
    operator fun set(type: StatType, stat: Stat): Stat?
}