package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import io.github.mg138.tsbook.stat.StatType
import java.util.*
import kotlin.math.roundToInt

class ItemIdentification : EnumMap<StatType, Float>(StatType::class.java) {
    operator fun set(key: StatType, value: Float): Float? {
        return super.put(key, value)
    }

    override operator fun get(key: StatType): Float {
        super.putIfAbsent(key, 0.5f)
        return super.get(key)!!
    }

    companion object {
        private val rand = Random()

        private fun randomPercentage(): Float {
            (0.5 + (rand.nextGaussian() / 4)).let {
                return when {
                    it < 0 -> 0F
                    it > 1 -> 1F
                    else -> (it * 100).roundToInt().toFloat() / 100
                }
            }
        }

        fun create(setting: StatedItemSetting, isRandom: Boolean): ItemIdentification {
            val identification = ItemIdentification()
            when {
                isRandom -> setting.stats.forEach { (type, _) -> identification[type] = randomPercentage() }
                else -> setting.stats.forEach { (type, _) -> identification[type] = 1F }
            }
            return identification
        }

        fun create(setting: ItemSetting, isRandom: Boolean): ItemIdentification? {
            return when (setting) {
                is StatedItemSetting -> create(setting, isRandom)
                else -> null
            }
        }
    }
}