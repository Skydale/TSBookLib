package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.stat.data.StatMap
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.stat.data.Stated
import io.github.mg138.tsbook.config.item.element.ItemSetting

abstract class ItemStated(
    setting: ItemSetting,
    private val statMap: StatMap
) : SimpleItem(setting), Stated {
    private fun putStatsInLore() {
        val lore = this.getLore()

        for (i in lore.indices) {
            statMap.types().forEach {
                lore[i] = statMap.applyPlaceholder(lore[i], it)
            }
        }
    }

    init {
        putStatsInLore()
    }

    override fun getStatResult(type: StatType) = statMap.getStatResult(type)
    override fun getStat(type: StatType) = statMap.getStat(type)
    override fun stats() = statMap.stats()
    override fun types() = statMap.types()
    override fun iterator() = statMap.iterator()
}