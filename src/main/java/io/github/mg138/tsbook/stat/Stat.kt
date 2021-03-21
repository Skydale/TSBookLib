package io.github.mg138.tsbook.stat

interface Stat {
    operator fun plus(increment: Stat?): Stat {
        return when (this) {
            is StatRange -> when (increment) {
                is StatRange -> this + increment
                null -> this
                else -> throw IllegalArgumentException("Incompatible Stat type. (StatRange verses ${increment.javaClass.name})")
            }
            is StatSingle -> when (increment) {
                is StatSingle -> this + increment
                null -> this
                else -> throw IllegalArgumentException("Incompatible Stat type. (StatSingle verses ${increment.javaClass.name})")
            }
            else -> this
        }
    }

    operator fun times(multiplier: Int): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    operator fun times(multiplier: Float): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    operator fun times(multiplier: Double): Stat {
        return when (this) {
            is StatRange -> this * multiplier
            is StatSingle -> this * multiplier
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Int): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Float): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    operator fun div(divisor: Double): Stat {
        return when (this) {
            is StatRange -> this / divisor
            is StatSingle -> this / divisor
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    var stat: Double
}