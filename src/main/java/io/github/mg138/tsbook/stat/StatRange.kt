package io.github.mg138.tsbook.stat

import java.util.*

data class StatRange(var max: Double, var min: Double) : Stat {
    override var stat
        get() = getStat(Random().nextDouble())
        set(value) {}

    operator fun plus(increment: StatRange?): StatRange {
        val that = this.copy()
        increment?.let {
            that.max += it.max
            that.min += it.min
        }
        return that
    }

    override operator fun times(multiplier: Int): StatRange {
        val that = this.copy()
        that.max *= multiplier
        that.min *= multiplier
        return that
    }

    override operator fun times(multiplier: Float): StatRange {
        val that = this.copy()
        that.max *= multiplier
        that.min *= multiplier
        return that
    }

    override operator fun times(multiplier: Double): StatRange {
        val that = this.copy()
        that.max *= multiplier
        that.min *= multiplier
        return that
    }

    override operator fun div(divisor: Int): StatRange {
        val that = this.copy()
        that.max /= divisor
        that.min /= divisor
        return that
    }

    override operator fun div(divisor: Float): StatRange {
        val that = this.copy()
        that.max /= divisor
        that.min /= divisor
        return that
    }

    override operator fun div(divisor: Double): StatRange {
        val that = this.copy()
        that.max /= divisor
        that.min /= divisor
        return that
    }

    private fun getStat(percentage: Double): Double {
        return if (max == min) max else percentage * (max - min) + min
    }

    init {
        if (max < min) {
            val temp = max
            max = min
            min = temp
        }
    }
}