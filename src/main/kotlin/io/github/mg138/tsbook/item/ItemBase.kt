package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.data.DataContainer
import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.data.Identifier
import org.bukkit.Material
import java.util.*

interface ItemBase : DataContainer {
    fun getName(): String
    fun getLore(): MutableList<String>
    fun getItemId(): Identifier
    fun getMaterial(): Material
    fun getModel(): Int
    fun getUUID(): UUID
    fun getItemType(): ItemType

    //override fun toString() = "ItemBase(id=$itemId)"

    fun newUUID(): UUID

    override fun getDataMap(): DataMap
}
