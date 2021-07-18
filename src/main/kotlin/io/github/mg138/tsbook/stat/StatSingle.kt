package io.github.mg138.tsbook.stat

class StatSingle(private val stat: Double) : Stat {
    override fun result() = stat

    override fun applyPlaceholder(string: String) = string.replace("[stat]", stat.toInt().toString())

    override operator fun plus(increment: Stat?): StatSingle {
        if (increment == null) return this

        when (increment) {
            is StatSingle -> return StatSingle(
                stat = stat + increment.stat
            )
            else -> throw IllegalArgumentException(incompatible(increment))
        }
    }

    override operator fun minus(decrement: Stat?): StatSingle {
        if (decrement == null) return this

        when (decrement) {
            is StatSingle -> return StatSingle(
                stat = stat - decrement.stat
            )
            else -> throw IllegalArgumentException(incompatible(decrement))
        }
    }

    override fun plus(increment: Number): Stat {
        val inc = increment.toDouble()

        if (inc == 0.0) return this

        return StatSingle(
            stat + inc
        )
    }

    override fun minus(decrement: Number): Stat {
        val dec = decrement.toDouble()

        if (dec == 0.0) return this

        return StatSingle(
            stat - dec
        )
    }

    override operator fun times(multiplier: Number): StatSingle {
        val mul = multiplier.toDouble()

        if (mul == 1.0) return this

        return StatSingle(
            stat = stat * mul
        )
    }

    override operator fun div(divisor: Number): StatSingle {
        val div = divisor.toDouble()

        if (div == 1.0) return this

        return StatSingle(
            stat = stat / div
        )
    }

    override fun ensureAtLeast(minimum: Double): Stat {
        if (this.stat < minimum) {
            return StatSingle(minimum)
        }
        return this
    }

    override fun compareTo(other: Stat): Int {
        return this.result().compareTo(other.result())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatSingle) return false

        return stat == other.stat
    }

    override fun hashCode() = stat.hashCode()

    override fun toString() = "StatSingle($stat)"
}