package io.github.mg138.tsbook.item.attribute.stat

data class StatSingle(private var stat: Double) : Stat {
    override fun getStat() = stat

    override fun applyPlaceholder(string: String) = string.replace("[stat]", stat.toInt().toString())

    override operator fun plus(increment: Stat?): StatSingle {
        if (increment == null) return this.copy()
        if (increment !is StatSingle) throw IllegalArgumentException(incompatible(increment))

        return this.copy().also {
            it.stat += increment.stat
        }
    }

    override operator fun minus(decrement: Stat?): StatSingle {
        if (decrement == null) return this.copy()
        if (decrement !is StatSingle) throw IllegalArgumentException(incompatible(decrement))

        return this.copy().also {
            it.stat -= decrement.stat
        }
    }

    override operator fun times(multiplier: Int): StatSingle {
        return this.copy().also {
            it.stat *= multiplier
        }
    }

    override operator fun times(multiplier: Float): StatSingle {
        return this.copy().also {
            it.stat *= multiplier
        }
    }

    override operator fun times(multiplier: Double): StatSingle {
        return this.copy().also {
            it.stat *= multiplier
        }
    }

    override operator fun div(divisor: Int): StatSingle {
        return this.copy().also {
            it.stat /= divisor
        }
    }

    override operator fun div(divisor: Float): StatSingle {
        return this.copy().also {
            it.stat /= divisor
        }
    }

    override operator fun div(divisor: Double): StatSingle {
        return this.copy().also {
            it.stat /= divisor
        }
    }
}