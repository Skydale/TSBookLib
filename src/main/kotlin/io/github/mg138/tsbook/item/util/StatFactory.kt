package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.attribute.stat.StatRange
import io.github.mg138.tsbook.item.attribute.stat.StatSingle
import io.github.mg138.tsbook.item.attribute.stat.StatType
import io.github.mg138.tsbook.item.data.Identification
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.setting.item.element.ItemStatedSetting
import org.bukkit.configuration.ConfigurationSection
import java.util.*

object StatFactory {
    private val rand = Random()

    private fun randomPercentage() = rand.nextInt(100000000).toFloat() / 100000000

    fun identifiedStat(setting: ConfigurationSection): IdentifiedStat {
        IdentifiedStat()
    }

    fun identification(setting: ItemStatedSetting, isRandom: Random): Identification {
        val identification = Identification()

        if (isRandom) {
            setting.stats.forEach { (type, _) -> identification[type] = randomPercentage() }

            else -> setting.stats.forEach { (type, _) -> identification[type] = 1F }
        }
        return identification
    }

    fun statMap(setting: ConfigurationSection): StatMap {
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