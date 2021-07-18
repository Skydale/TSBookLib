package io.github.mg138.tsbook.stat

interface Stat : Comparable<Stat> {
    fun result(): Double

    fun incompatible(that: Stat) =
        "Incompatible Stat type. (${this::class.simpleName} verses ${that::class.simpleName})"

    fun applyPlaceholder(string: String): String

    fun ensureAtLeast(minimum: Double): Stat

    operator fun plus(increment: Stat?): Stat
    operator fun minus(decrement: Stat?): Stat

    /**
     * Basic math operations.
     *
     * Each of these actions will return the Stat itself if the operation was meaningless.
     * Such as:
     * * stat + 0
     * * stat - 0
     * * stat / 1
     * * stat * 1
     */
    operator fun plus(increment: Number): Stat
    operator fun minus(decrement: Number): Stat
    operator fun times(multiplier: Number): Stat
    operator fun div(divisor: Number): Stat
}