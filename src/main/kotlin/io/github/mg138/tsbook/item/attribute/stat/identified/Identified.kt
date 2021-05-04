package io.github.mg138.tsbook.item.attribute.stat.identified

import io.github.mg138.tsbook.item.attribute.stat.data.Stated
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.item.attribute.stat.identified.data.Identification

interface Identified : Stated {
    fun getIden(type: StatType): Float
    fun getIden(): Identification
}