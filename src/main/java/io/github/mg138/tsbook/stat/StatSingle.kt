package io.github.mg138.tsbook.stat

data class StatSingle(override var stat: Double) : Stat {
    operator fun plus(increment: StatSingle?): StatSingle {
        val that = this.copy()
        increment?.let {
            that.stat += increment.stat
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