package io.github.mg138.tsbook.players.data

import io.github.mg138.tsbook.item.attribute.data.ItemType
import io.github.mg138.tsbook.item.ItemBase
import java.util.*

class ItemInstanceMapWrapper: HashMap<Int, ItemBase>() {
    fun containsType(itemType: ItemType): Boolean {
        this.forEach { (_, inst) -> if (inst.itemType == itemType) return true }
        return false
    }

    fun getByType(itemType: ItemType): Int {
        this.forEach { (i, inst) -> if (inst.itemType == itemType) return i }
        return -1
    }
}