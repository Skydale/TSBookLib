package io.github.mg138.tsbook.stat

data class StatSingle(private var stat: Double) : Stat {
    override fun getStat() = stat

    override fun applyPlaceholder(string: String) = string.replace("[stat]", stat.toInt().toString())

    operator fun plus(increment: StatSingle?): StatSingle {
        val that = this.copy()
        increment?.let {
            that.stat += increment.getStat()
        }
        return that
    }

    override operator fun times(multiplier: Int): StatSingle {
        val that = this.copy()
        that.stat *= multiplier
        return that
    }

    override operator fun times(multiplier: Float): StatSingle {
        val that = this.copy()
        that.stat *= multiplier
        return that
    }

    override operator fun times(multiplier: Double): StatSingle {
        val that = this.copy()
        that.stat *= multiplier
        return that
    }

    override operator fun div(divisor: Int): StatSingle {
        val that = this.copy()
        that.stat /= divisor
        return that
    }

    override operator fun div(divisor: Float): StatSingle {
        val that = this.copy()
        that.stat /= divisor
        return that
    }

    override operator fun div(divisor: Double): StatSingle {
        val that = this.copy()
        that.stat /= divisor
        return that
    }
}