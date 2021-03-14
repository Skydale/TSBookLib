package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.Book.Companion.setting
import io.github.mg138.tsbook.items.data.stat.Stat
import io.github.mg138.tsbook.items.data.stat.StatMap
import io.github.mg138.tsbook.items.data.stat.StatRange
import io.github.mg138.tsbook.items.data.stat.StatType
import io.github.mg138.tsbook.setting.AbstractSetting
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting

class ItemStats(val identification: ItemIdentification, private val config: AbstractSetting, val statMap: StatMap) {
    operator fun get(type: StatType): Double {
        return statMap[type]!!.stat * identification[type]
    }

    fun translate(type: StatType, stat: Stat): String {
        val format = config.translate.translate("format.$type")
        val percentage = identification[type].toDouble()

        return if (stat is StatRange) {
            format
                .replace("[min]", (stat.min * percentage).toInt().toString())
                .replace("[max]", (stat.max * percentage).toInt().toString())
                .replace("[percentage]", (percentage * 100).toInt().toString() + '%')
        } else {
            format
                .replace("[stat]", (stat.stat * percentage).toInt().toString())
                .replace("[percentage]", (percentage * 100).toInt().toString() + '%')
        }
    }

    companion object {
        fun create(identification: ItemIdentification?, config: AbstractSetting, setting: ItemSetting): ItemStats? {
            return if (setting is StatedItemSetting && identification != null) {
                ItemStats(identification, config, setting.stats)
            } else null
        }

        fun create(config: AbstractSetting, setting: ItemSetting, isRandom: Boolean): ItemStats? {
            return create(
                ItemIdentification.create(setting, isRandom),
                config,
                setting
            )
        }

        fun create(identification: ItemIdentification?, config: AbstractSetting, ID: String): ItemStats? {
            return create(
                identification,
                config,
                setting.itemConfig.getAnyItemByID(ID)
            )
        }
    }
}