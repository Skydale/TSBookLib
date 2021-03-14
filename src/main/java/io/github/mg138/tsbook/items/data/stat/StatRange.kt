package io.github.mg138.tsbook.items.data.stat

import java.util.*

class StatRange(max: Double, min: Double) : Stat {
    val max: Double
    val min: Double

    override val stat: Double
        get() = if (max == min) { max } else { getStat(Random().nextDouble()) }

    private fun getStat(percentage: Double): Double {
        return if (max == min) max else percentage * (max - min) + min
    }

    init {
        if (max > min) {
            this.max = max
            this.min = min
        } else {
            this.max = min
            this.min = max
        }
    }
}