package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.item.attribute.stat.*
import io.github.mg138.tsbook.item.data.Identification
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.setting.item.element.ItemStatedSetting
import org.bukkit.configuration.ConfigurationSection
import java.util.*

object StatFactory {
    private val rand = Random()

    private fun randomPercentage() = rand.nextInt(100000000).toFloat() / 100000000

    fun stat(name: String, setting: ConfigurationSection): Stat {
        return when {
            setting.contains("$name.min") -> StatRange(
                setting.getDouble("$name.max"),
                setting.getDouble("$name.min")
            )
            else -> StatSingle(
                setting.getDouble(name)
            )
        }
    }

    fun identification(setting: ItemStatedSetting, isRandom: Boolean): Identification {
        val identification = Identification()

        if (isRandom) {
            setting.stats.forEach { (type, _) ->
                identification[type] = randomPercentage()
            }
        } else {
            setting.stats.forEach { (type, _) ->
                identification[type] = 1F
            }
        }

        return identification
    }

    fun statMap(setting: ConfigurationSection): StatMap {
        val stats = StatMap()

        setting.getKeys(false).forEach {
            stats[StatType.valueOf(it)] = stat(it, setting)
        }

        return stats
    }

    fun identifiedStat(setting: ItemStatedSetting, isRandom: Boolean): IdentifiedStat {
        return IdentifiedStat(setting.stats, identification(setting, isRandom))
    }
}