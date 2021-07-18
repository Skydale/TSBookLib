package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.config.item.element.ItemSetting
import java.util.*

abstract class SimpleItem(
    protected val setting: ItemSetting
) : ItemBase {
    private var uuid: UUID = UUID.randomUUID()

    override fun getName() = setting.name
    override fun getLore() = setting.lore
    override fun getItemId() = setting.itemId
    override fun getMaterial() = setting.material
    override fun getModel() = setting.model
    override fun getUUID() = uuid

    override fun newUUID(): UUID {
        return UUID.randomUUID().also { uuid = it }
    }

    override fun getDataMap() = DataMap()
}