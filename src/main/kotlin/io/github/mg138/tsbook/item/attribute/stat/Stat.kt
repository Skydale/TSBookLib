package io.github.mg138.tsbook.item.attribute.stat

interface Stat {
    fun getStat(): Double

    fun unsupported(action: String) =
        "Unsupported action `${action}` for (${this::class.simpleName}) (Did someone forget to add it?)"

    fun incompatible(that: Stat) =
        "Incompatible Stat type. (${this::class.simpleName} verses ${that::class.simpleName})"

    fun applyPlaceholder(string: String): String {
        throw IllegalArgumentException(unsupported("applyPlaceholder"))
    }

    operator fun plus(increment: Stat?): Stat {
        throw IllegalArgumentException(unsupported("plus"))
    }

    operator fun minus(decrement: Stat?): Stat {
        throw IllegalArgumentException(unsupported("plus"))
    }

    operator fun times(multiplier: Int): Stat {
        throw IllegalArgumentException(unsupported("times Int"))
    }

    operator fun times(multiplier: Float): Stat {
        throw IllegalArgumentException(unsupported("times Float"))
    }

    operator fun times(multiplier: Double): Stat {
        throw IllegalArgumentException(unsupported("times Double"))
    }

    operator fun div(divisor: Int): Stat {
        throw IllegalArgumentException(unsupported("div by Int"))
    }

    operator fun div(divisor: Float): Stat {
        throw IllegalArgumentException(unsupported("div by Float"))
    }

    operator fun div(divisor: Double): Stat {
        throw IllegalArgumentException(unsupported("div by Double"))
    }
}