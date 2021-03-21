package io.github.mg138.tsbook.setting.mob.element

import io.github.mg138.tsbook.items.ItemIdentification
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.stat.StatMap
import io.github.mg138.tsbook.stat.StatSingle
import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.configuration.ConfigurationSection

class MobSetting(val itemStats: ItemStats) {
    companion object {
        fun create(setting: ConfigurationSection): MobSetting {
            val stats = StatMap()
            val identification = ItemIdentification()

            for (literalType in setting.getKeys(false)) {
                val type = StatType.valueOf(literalType.toUpperCase())
                stats[type] = StatSingle(setting.getDouble(literalType))
                identification[type] = 1f
            }
            return MobSetting(ItemStats(stats, identification, BookConfig))
        }
    }
}