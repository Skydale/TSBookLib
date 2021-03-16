package io.github.mg138.tsbook.items.data.stat

import org.bukkit.configuration.ConfigurationSection
import java.util.*

class StatMap : HashMap<StatType, Stat>() {
    companion object {
        fun from(setting: ConfigurationSection): StatMap {
            val stats = StatMap()

            for (literalType in setting.getKeys(false)) {
                val type = StatType.valueOf(literalType.toUpperCase())
                val stat: Stat = if (setting.contains("$literalType.min")) {
                    StatRange(
                        setting.getDouble("$literalType.max"),
                        setting.getDouble("$literalType.min")
                    )
                } else {
                    StatSingle(
                        setting.getDouble(literalType)
                    )
                }
                stats[type] = stat
            }
            return stats
        }
    }
}