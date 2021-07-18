package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.config.BookConfig
import io.github.mg138.tsbook.util.PresetUtil

interface ItemType {
    private val noNameMessage: String
        get() = "ItemType ${this.id} doesn't have a name!"

    val name: String
    
    val id: String

    object Preset {
        val UNKNOWN = object : ItemType {
            override val id = "UNKNOWN"
        }

        val BOW = object : ItemType {
            override val id = "BOW"
        }

        val UNIDENTIFIED = object : ItemType {
            override val id = "UNIDENTIFIED"
        }

        val types = PresetUtil.getObjectPropertiesOfType<ItemType, Preset>()
    }
}