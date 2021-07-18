package io.github.mg138.tsbook.stat.util

import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.StatRange
import io.github.mg138.tsbook.stat.StatSingle
import io.github.mg138.tsbook.stat.data.*
import io.github.mg138.tsbook.stat.identified.data.Identification
import io.github.mg138.tsbook.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.stat.type.api.StatTypeManager
import io.github.mg138.tsbook.config.item.element.ItemStatedSetting
import org.bukkit.configuration.ConfigurationSection
import java.util.*

object StatConstructor {
    private val rand = Random()

    private fun randomPercentage() = rand.nextInt(101).toFloat() / 100

    fun stat(name: String, setting: ConfigurationSection): Stat {
        return when {
            setting.contains("$name.min") -> StatRange(
                setting.getDouble("$name.min"),
                setting.getDouble("$name.max")
            )
            else -> StatSingle(
                setting.getDouble(name)
            )
        }
    }

    fun identification(setting: ItemStatedSetting, isRandom: Boolean): Identification {
        val identification = Identification()

        if (isRandom) {
            setting.statMap.types().forEach {
                identification[it] = randomPercentage()
            }
        } else {
            setting.statMap.types().forEach {
                identification[it] = 1F
            }
        }

        return identification
    }

    fun statMap(setting: ConfigurationSection): StatMap {
        val stats = StatMap()

        setting.getKeys(false).forEach { name ->
            StatTypeManager[name]?.let { type ->
                stats.putStat(type, stat(name, setting))
            }
        }

        return stats
    }

    fun identifiedStat(setting: ItemStatedSetting, isRandom: Boolean): IdentifiedStat {
        return IdentifiedStat(setting.statMap, identification(setting, isRandom))
    }

    fun identifiedStat(setting: ItemStatedSetting, dataMap: DataMap?): IdentifiedStat {
        val iden = Identification.fromDataMap(dataMap) ?: Identification()
        return IdentifiedStat(setting.statMap, iden)
    }
}