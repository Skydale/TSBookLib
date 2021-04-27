package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.item.attribute.stat.StatType
import io.github.mg138.tsbook.item.data.Identification

interface Identified : Stated {
    fun getIden(type: StatType): Float
    fun getIden(): Identification
}