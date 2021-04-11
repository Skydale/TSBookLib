package io.github.mg138.tsbook.attribute.stat

interface Stat {
    fun getStat(): Double

    fun applyPlaceholder(string: String): String {
        return when (this) {
            is StatRange -> this.applyPlaceholder(string)
            is StatSingle -> this.applyPlaceholder(string)
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun plus(increment: Stat?): Stat {
        return when (this) {
            is StatRange -> when (increment) {
                is StatRange -> this + increment
                null -> this
                else -> throw IllegalArgumentException("Incompatible Stat type. (${this::class.simpleName} verses ${increment::class.simpleName})")
            }
            is StatSingle -> when (increment) {
                is StatSingle -> this + increment
                null -> this
                else -> throw IllegalArgumentException("Incompatible Stat type. (${this::class.simpleName} verses ${increment::class.simpleName})")
            }
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun times(multiplier: Int): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun times(multiplier: Float): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun times(multiplier: Double): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Int): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Float): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Double): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (${this::class.simpleName}) (Did someone forget to add it?)")
        }
    }
}