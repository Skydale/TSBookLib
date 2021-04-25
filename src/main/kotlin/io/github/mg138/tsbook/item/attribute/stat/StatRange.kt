package io.github.mg138.tsbook.item.attribute.stat

import java.util.*

data class StatRange(var max: Double, var min: Double) : Stat {
    private val rand = Random()

    init {
        if (max < min) {
            val temp = max
            max = min
            min = temp
        }
    }

    private fun withPercentage(percentage: Double) = if (max == min) max else percentage * (max - min) + min

    override fun getStat() = this.withPercentage(rand.nextDouble())

    override fun applyPlaceholder(string: String) = string
        .replace("[min]", min.toLong().toString())
        .replace("[max]", max.toLong().toString())

    override operator fun plus(increment: Stat?): StatRange {
        if (increment == null) return this.copy()
        if (increment !is StatRange) throw IllegalArgumentException(incompatible(increment))

        return this.copy().also {
            it.max += increment.max
            it.min += increment.min
        }
    }

    override operator fun minus(decrement: Stat?): StatRange {
        if (decrement == null) return this.copy()
        if (decrement !is StatRange) throw IllegalArgumentException(incompatible(decrement))

        return this.copy().also {
            it.max -= decrement.max
            it.min -= decrement.min
        }
    }

    override operator fun times(multiplier: Int): StatRange {
        return this.copy().also {
            it.max *= multiplier
            it.min *= multiplier
        }
    }

    override operator fun times(multiplier: Float): StatRange {
        return this.copy().also {
            it.max *= multiplier
            it.min *= multiplier
        }
    }

    override operator fun times(multiplier: Double): StatRange {
        return this.copy().also {
            it.max *= multiplier
            it.min *= multiplier
        }
    }

    override operator fun div(divisor: Int): StatRange {
        return this.copy().also {
            it.max /= divisor
            it.min /= divisor
        }
    }

    override operator fun div(divisor: Float): StatRange {
        return this.copy().also {
            it.max /= divisor
            it.min /= divisor
        }
    }

    override operator fun div(divisor: Double): StatRange {
        return this.copy().also {
            it.max /= divisor
            it.min /= divisor
        }
    }
}