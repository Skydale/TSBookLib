package io.github.mg138.tsbook.item.attribute.stat

interface Stat {
    fun getStat(): Double

    fun incompatible(that: Stat) =
        "Incompatible Stat type. (${this::class.simpleName} verses ${that::class.simpleName})"

    fun applyPlaceholder(string: String): String

    operator fun plus(increment: Stat?): Stat

    operator fun minus(decrement: Stat?): Stat

    operator fun times(multiplier: Int): Stat

    operator fun times(multiplier: Float): Stat

    operator fun times(multiplier: Double): Stat

    operator fun div(divisor: Int): Stat

    operator fun div(divisor: Float): Stat

    operator fun div(divisor: Double): Stat
}