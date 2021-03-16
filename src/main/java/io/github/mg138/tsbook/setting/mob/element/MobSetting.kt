package io.github.mg138.tsbook.setting.mob.element

import io.github.mg138.tsbook.items.ItemIdentification
import io.github.mg138.tsbook.items.data.stat.StatMap
import io.github.mg138.tsbook.items.data.stat.StatSingle
import io.github.mg138.tsbook.items.data.stat.StatType
import org.bukkit.configuration.ConfigurationSection

class MobSetting(setting: ConfigurationSection) {
    val stats = StatMap()
    val identification = ItemIdentification()

    init {
        for (literalType in setting.getKeys(false)) {
            val type = StatType.valueOf(literalType.toUpperCase())
            stats[type] = StatSingle(setting.getDouble(literalType))
            identification[type] = 1f
        }
    }
}