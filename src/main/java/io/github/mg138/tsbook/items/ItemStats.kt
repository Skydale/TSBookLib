package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.setting.AbstractConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import io.github.mg138.tsbook.stat.*
import java.lang.IllegalArgumentException

class ItemStats(initStats: StatMap, val identification: ItemIdentification, private val config: AbstractConfig) {
    val stats = StatMap()

    init {
        initStats.forEach { (type, stat) ->
            val multiplier = identification[type]
            stats[type] = when {
                multiplier == 0.0F -> stat
                multiplier > 0.0F -> stat * multiplier
                else -> throw IllegalArgumentException("Identification must be >= 0! (is $multiplier, keyed to $type)")
            }
        }
    }

    operator fun get(type: StatType): Stat {
        return stats[type] ?: throw NullPointerException("No value keyed to $type.")
    }

    fun translate(type: StatType, stat: Stat): String {
        val format = config.translate.translate("format.$type")
        val percentage = identification[type]
        return when (val that = stat * percentage) {
            is StatRange -> {
                format
                    .replace("[min]", (that.min).toInt().toString())
                    .replace("[max]", (that.max).toInt().toString())
                    .replace("[percentage]", (percentage * 100).toInt().toString() + '%')
            }
            is StatSingle -> {
                format
                    .replace("[stat]", (that.stat).toInt().toString())
                    .replace("[percentage]", (percentage * 100).toInt().toString() + '%')
            }
            else -> throw IllegalArgumentException("Unsupported implementation of Stat interface. (Did someone forget to add it?)")
        }
    }

    companion object {
        fun create(setting: ItemSetting, identification: ItemIdentification?, config: AbstractConfig): ItemStats? {
            return if (setting is StatedItemSetting && identification != null) {
                ItemStats(setting.stats, identification, config)
            } else null
        }

        fun create(setting: ItemSetting, config: AbstractConfig, isRandom: Boolean): ItemStats? {
            return create(
                setting,
                ItemIdentification.create(setting, isRandom),
                config
            )
        }

        fun create(identification: ItemIdentification?, config: AbstractConfig, ID: String): ItemStats? {
            return create(
                ItemConfig.getAnyItemByID(ID),
                identification,
                config
            )
        }
    }
}