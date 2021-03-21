package io.github.mg138.tsbook.players.data

import io.github.mg138.tsbook.items.ItemInstance
import java.util.*

class ItemInstanceMapWrapper: HashMap<Int, ItemInstance>() {
    fun containsType(type: String): Boolean {
        this.forEach { (_, inst) -> if (inst.itemType == type) return true }
        return false
    }

    fun getByType(type: String): Int {
        this.forEach { (i, inst) -> if (inst.itemType == type) return i }
        return -1
    }
}