package io.github.mg138.tsbook.stat

import java.util.*

class StatRange(min: Double, max: Double) : Stat {
    val min: Double
    val max: Double

    val middle by lazy { this.withPercentage(0.5) }

    private val rand = Random()

    init {
        if (max < min) {
            this.min = max
            this.max = min
        } else {
            this.min = min
            this.max = max
        }
    }

    private fun withPercentage(percentage: Double): Double {
        if (min == max) return max

        return (percentage * (max - min)) + min
    }

    override fun result() = this.withPercentage(rand.nextDouble())

    override fun applyPlaceholder(string: String) = string
        .replace("[min]", min.toLong().toString())
        .replace("[max]", max.toLong().toString())

    override operator fun plus(increment: Stat?): StatRange {
        if (increment == null) return this

        when (increment) {
            is StatRange -> return StatRange(
                min = min + increment.min,
                max = max + increment.max
            )
            else -> throw IllegalArgumentException(incompatible(increment))
        }
    }

    override operator fun minus(decrement: Stat?): StatRange {
        if (decrement == null) return this

        when (decrement) {
            is StatRange -> return StatRange(
                min = min - decrement.min,
                max = max - decrement.max
            )
            else -> throw IllegalArgumentException(incompatible(decrement))
        }
    }

    override fun plus(increment: Number): Stat {
        val inc = increment.toDouble()

        if (inc == 0.0) return this

        return StatRange(
            min = min + inc,
            max = max + inc
        )
    }

    override fun minus(decrement: Number): Stat {
        val dec = decrement.toDouble()

        if (dec == 0.0) return this

        return StatRange(
            min = min - dec,
            max = max - dec
        )
    }

    override operator fun times(multiplier: Number): StatRange {
        val mul = multiplier.toDouble()

        if (mul == 1.0) return this

        return StatRange(
            min = min * mul,
            max = max * mul
        )
    }

    override operator fun div(divisor: Number): StatRange {
        val div = divisor.toDouble()

        if (div == 1.0) return this

        return StatRange(
            min = min / div,
            max = max / div
        )
    }


    override fun ensureAtLeast(minimum: Double): Stat {
        if (this.min < minimum) {
            return if (this.max > minimum) {
                StatRange(
                    min = minimum,
                    max = this.max
                )
            } else {
                StatRange(
                    min = minimum,
                    max = minimum
                )
            }
        }
        return this
    }

    override fun compareTo(other: Stat): Int {
        if (other is StatRange) {
            return middle.compareTo(other.middle)
        }
        return middle.compareTo(other.result())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatRange) return false

        return min == other.min && max == other.max
    }

    override fun hashCode() = 31 * min.hashCode() + max.hashCode()

    override fun toString() = "StatRange($min, $max)"
}