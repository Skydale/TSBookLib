package io.github.mg138.tsbook.stat

import org.bukkit.configuration.ConfigurationSection

class StatMap : HashMap<StatType, Stat>() {
    @Deprecated("Don't use this directly...", ReplaceWith("getStatOut"))
    override fun get(key: StatType): Stat? {
        return super.get(key)
    }

    fun getStatOut(type: StatType): Double {
        @Suppress("DEPRECATION")
        return get(type).let {
            when (it) {
                null -> 0.0
                else -> it.stat
            }
        }
    }

    companion object {
        fun from(setting: ConfigurationSection): StatMap {
            val stats = StatMap()

            setting.getKeys(false).forEach { literalType ->
                val type = StatType.valueOf(literalType.toUpperCase())

                stats[type] = when {
                    setting.contains("$literalType.min") -> {
                        StatRange(
                            setting.getDouble("$literalType.max"),
                            setting.getDouble("$literalType.min")
                        )
                    }
                    else -> {
                        StatSingle(
                            setting.getDouble(literalType)
                        )
                    }
                }
            }

            return stats
        }
    }
}