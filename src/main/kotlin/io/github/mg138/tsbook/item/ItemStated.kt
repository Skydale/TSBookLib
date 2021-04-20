package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.attribute.Stated
import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.attribute.stat.StatType
import io.github.mg138.tsbook.item.attribute.stat.util.StatTables
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
            val s = lore[i]
            stats.forEach { (type, _) ->
                StatTables.placeholders[type]?.let {
                    if (s.contains(it)) {
                        stats.translate(type)?.let { translated ->
                            lore[i] = s.replace(it, translated)
                        }
                    }
                }
            }
        }
    }

    override fun getStatOut(type: StatType) = stats.getStatOut(type)

    override fun getStat(type: StatType) = stats[type]
}