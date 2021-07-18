package io.github.mg138.tsbook.item.item

import io.github.mg138.tsbook.item.ItemIdentified
import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.item.interact.Clickable
import io.github.mg138.tsbook.config.item.element.ItemSetting

abstract class NormalItem(
    setting: ItemSetting,
    identifiedStat: IdentifiedStat
) : ItemIdentified(setting, identifiedStat), Clickable {
    companion object {
        fun getItemType() = ItemType.Preset.UNKNOWN
    }
}