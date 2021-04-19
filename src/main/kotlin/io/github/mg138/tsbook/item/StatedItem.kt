package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.attribute.stat.util.StatTables
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import java.util.*

class StatedItem(setting: ItemSetting, uuid: UUID, val identifiedStat: IdentifiedStat): ItemInstance(setting, uuid) {
    init {
        putStatsInLore()
    }

    private fun putStatsInLore() {
        for (i in lore.indices) {
            val s = lore[i]
            identifiedStat.forEach { (type, _) ->
                StatTables.placeholders[type]?.let {
                    if (s.contains(it)) {
                        lore[i] = s.replace(it, identifiedStat.translate(type))
                        println(identifiedStat.translate(type))
                    }
                }
            }
        }
    }
}