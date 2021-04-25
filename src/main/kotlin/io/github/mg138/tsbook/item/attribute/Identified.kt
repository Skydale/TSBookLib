package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.item.attribute.stat.StatType

interface Identified : Stated {
    fun getIden(type: StatType): Float
    fun getAllIden(): Map<StatType, Float>
}