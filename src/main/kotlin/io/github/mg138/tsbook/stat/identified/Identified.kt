package io.github.mg138.tsbook.stat.identified

import io.github.mg138.tsbook.stat.data.Stated
import io.github.mg138.tsbook.stat.identified.data.Identification
import io.github.mg138.tsbook.stat.type.StatType

interface Identified : Stated {
    fun getIden(type: StatType): Float
    fun getIden(): Identification
}