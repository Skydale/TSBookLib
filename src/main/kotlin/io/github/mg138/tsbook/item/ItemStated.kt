package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.attribute.stat.Stated
import io.github.mg138.tsbook.item.attribute.stat.data.StatMap
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import java.util.*

open class ItemStated(
    setting: ItemSetting, uuid: UUID, private val stats: StatMap
): ItemBase(setting, uuid), Stated {
    init {
        putStatsInLore()
    }

    private fun putStatsInLore() {
        for (i in lore.indices) {
            stats.forEach { (type, _) ->
                lore[i] = stats.applyPlaceholder(lore[i], type)
            }
        }
    }

    override fun getStatOut(type: StatType) = stats.getStatOut(type)
    override fun getStat(type: StatType) = stats.getStat(type)
}