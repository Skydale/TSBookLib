package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.stat.identified.Identified
import io.github.mg138.tsbook.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.config.item.element.ItemSetting

abstract class ItemIdentified(
    setting: ItemSetting, identifiedStat: IdentifiedStat
) : ItemStated(setting, identifiedStat), Identified {
    private val identification = identifiedStat.getIden()

    override fun getIden(type: StatType) = identification[type]
    override fun getIden() = identification

    override fun getDataMap(): DataMap {
        return super.getDataMap().also {
            it += identification
        }
    }
}