package io.github.mg138.tsbook.setting.mob.element

import io.github.mg138.tsbook.stat.StatMap
import io.github.mg138.tsbook.stat.StatSingle
import io.github.mg138.tsbook.stat.StatType
import org.bukkit.configuration.ConfigurationSection

class MobSetting(val stats: StatMap) {
    companion object {
        fun create(setting: ConfigurationSection): MobSetting {
            val stats = StatMap()

            for (literalType in setting.getKeys(false)) {
                val type = StatType.valueOf(literalType.toUpperCase())
                stats[type] = StatSingle(setting.getDouble(literalType))
            }
            return MobSetting(stats)
        }
    }
}