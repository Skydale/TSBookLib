package io.github.mg138.tsbook.item.item.factory

import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.data.Identifier
import io.github.mg138.tsbook.item.item.NormalItem

interface ItemFactory<T: NormalItem> {
    val namespace: String

    fun makeItem(id: String, dataMap: DataMap?): T?
}