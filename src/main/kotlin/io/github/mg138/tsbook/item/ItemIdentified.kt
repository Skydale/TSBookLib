package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.item.attribute.stat.identified.Identified
import io.github.mg138.tsbook.item.attribute.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.setting.item.element.ItemStatedSetting
import java.util.*

class ItemIdentified(
    setting: ItemStatedSetting, uuid: UUID, identifiedStat: IdentifiedStat
) : ItemStated(setting, uuid, identifiedStat), Identified {
    private val identification = identifiedStat.getIden()

    override fun getIden(type: StatType) = identification[type]
    override fun getIden() = identification
}