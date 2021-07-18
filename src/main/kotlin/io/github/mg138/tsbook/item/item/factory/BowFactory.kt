package io.github.mg138.tsbook.item.item.factory

import io.github.mg138.tsbook.config.item.element.ItemSetting
import io.github.mg138.tsbook.config.item.element.ItemStatedSetting
import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.item.item.Bow
import io.github.mg138.tsbook.stat.util.StatConstructor

class BowFactory : ItemFactory<Bow> {
    override val itemType = ItemType.Preset.BOW

    override fun makeItem(setting: ItemSetting, dataMap: DataMap?): Bow? {
        if (setting !is ItemStatedSetting) return null

        val identification = StatConstructor.identifiedStat(setting, dataMap)
        return Bow(setting, identification)
    }
}