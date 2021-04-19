package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.item.data.Identification
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import java.util.*
import kotlin.math.roundToInt

object ItemFactory {
    private val rand = Random()
    private const val shift = 0.5
    private const val spread = 0.5

    private fun randomPercentage(): Float {

    }

    private fun randomPercentage() = ((rand.nextGaussian() / spread) + shift).let {
            when {
                it < 0 -> 0F
                it > 1 -> 1F
                else -> (it * 100).roundToInt().toFloat() / 100
            }
        }

    fun create(setting: StatedItemSetting, isRandom: Boolean): Identification {
        val identification = Identification()
        when {
            isRandom -> setting.stats.forEach { (type, _) -> identification[type] = randomPercentage() }
            else -> setting.stats.forEach { (type, _) -> identification[type] = 1F }
        }
        return identification
    }

    fun create(setting: ItemSetting, isRandom: Boolean): Identification? {
        return if (setting is StatedItemSetting) {
            create(setting, isRandom)
        } else null
    }

    fun createItem(id: String, identifiedStat: IdentifiedStat?, uuid: UUID): ItemInstance {
        return ItemInstance(
            ItemConfig.getAnyItemByID(id),
            identifiedStat,
            uuid
        )
    }

    fun createStat(setting: ItemSetting, identification: Identification?): IdentifiedStat? {
        return when {
            setting is StatedItemSetting && identification != null -> IdentifiedStat(setting.stats, identification)
            else -> null
        }
    }

    fun createStat(setting: ItemSetting, isRandom: Boolean): IdentifiedStat? {
        return createStat(
            setting,
            Identification.create(setting, isRandom)
        )
    }

    fun createStat(id: String, identification: Identification?): IdentifiedStat? {
        return createStat(
            ItemConfig.getAnyItemByID(id),
            identification
        )
    }

    fun createStat(id: String, isRandom: Boolean): IdentifiedStat? {
        return createStat(
            ItemConfig.getAnyItemByID(id), isRandom
        )
    }
}