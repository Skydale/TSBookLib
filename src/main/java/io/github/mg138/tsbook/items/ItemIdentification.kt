package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import java.util.*
import kotlin.math.roundToInt

class ItemIdentification {
    private val percentageMap = EnumMap<StatType, Float>(StatType::class.java)

    private constructor(setting: StatedItemSetting, isRandom: Boolean) {
        if (isRandom) {
            val random = Random()
            setting.stats.forEach { (statType: StatType, _: Stat) ->
                var percentage = (((100 * (random.nextGaussian() / 4 + 0.5)).roundToInt().toFloat()) / 100)
                if (percentage < 0) percentage = 0f
                else if (percentage > 1) percentage = 1f
                percentageMap[statType] = percentage
            }
        } else {
            setting.stats.forEach { (statType: StatType, _: Stat) -> percentageMap[statType] = 1f }
        }
    }

    constructor(percentageMap: Map<StatType, Float>) {
        this.percentageMap.putAll(percentageMap)
    }

    constructor()

    operator fun set(type: StatType, percentage: Float): Float? {
        return percentageMap.put(type, percentage)
    }

    operator fun get(statType: StatType): Float {
        percentageMap.putIfAbsent(statType, 0.5f)
        return percentageMap[statType]!!
    }

    companion object {
        fun create(setting: ItemSetting, isRandom: Boolean): ItemIdentification? {
            return if (setting is StatedItemSetting) {
                ItemIdentification(setting, isRandom)
            } else null
        }
    }
}