package io.github.mg138.tsbook.item.attribute.stat

import org.bukkit.configuration.ConfigurationSection
import java.util.*

class StatMap : EnumMap<StatType, Stat>(StatType::class.java) {
    override fun putAll(from: Map<out StatType, Stat>) {
        from.forEach { (type, stat) ->
            this[type] = stat + this[type]
        }
    }

    fun getStatOut(type: StatType): Double {
        return this[type].let {
            when (it) {
                null -> 0.0
                else -> it.getStat()
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