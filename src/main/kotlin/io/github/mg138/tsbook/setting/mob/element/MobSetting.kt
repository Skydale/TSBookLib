package io.github.mg138.tsbook.setting.mob.element

import io.github.mg138.tsbook.item.attribute.stat.data.StatMap
import io.github.mg138.tsbook.item.attribute.stat.data.StatSingle
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import org.bukkit.configuration.ConfigurationSection

class MobSetting(val stats: StatMap) {
    companion object {
        fun create(setting: ConfigurationSection): MobSetting {
            val stats = StatMap()

            for (literalType in setting.getKeys(false)) {
                val type = StatType.valueOf(literalType.toUpperCase())
                stats.putStat(type, StatSingle(setting.getDouble(literalType)))
            }
            return MobSetting(stats)
        }
    }
}