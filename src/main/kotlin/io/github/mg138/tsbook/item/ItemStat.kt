package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.attribute.stat.Stat
import io.github.mg138.tsbook.attribute.stat.StatMap
import io.github.mg138.tsbook.attribute.stat.StatType
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import java.util.*

class ItemStat(
    private val initStats: StatMap,
    val identification: ItemIdentification
) : Iterable<Map.Entry<StatType, Stat>> {
    fun getStatOut(type: StatType): Double {
        return initStats.getStatSafe(type) * identification[type]
    }

    operator fun get(type: StatType): Stat {
        return initStats[type]!! * identification[type]
    }

    fun translate(type: StatType): String {
        return this[type]
            .applyPlaceholder(type.getFormat())
            .replace("[name]", type.toString())
            .replace("[percentage]", (identification[type] * 100).toInt().toString() + '%')
    }

    override fun iterator(): Iterator<Map.Entry<StatType, Stat>> {
        return object : Iterator<Map.Entry<StatType, Stat>> {
            val types = initStats.keys.iterator()

            override fun hasNext(): Boolean {
                return types.hasNext()
            }

            override fun next(): Map.Entry<StatType, Stat> {
                return types.next().let {
                    AbstractMap.SimpleImmutableEntry<StatType, Stat>(it, this@ItemStat[it])
                }
            }
        }
    }

    companion object {
        fun create(setting: ItemSetting, identification: ItemIdentification?): ItemStat? {
            return if (setting is StatedItemSetting && identification != null) {
                ItemStat(setting.stats, identification)
            } else null
        }

        fun create(setting: ItemSetting, isRandom: Boolean): ItemStat? {
            return create(
                setting,
                ItemIdentification.create(setting, isRandom)
            )
        }

        fun create(identification: ItemIdentification?, ID: String): ItemStat? {
            return create(
                ItemConfig.getAnyItemByID(ID),
                identification
            )
        }
    }
}