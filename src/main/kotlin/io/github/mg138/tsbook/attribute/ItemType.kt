package io.github.mg138.tsbook.attribute

import io.github.mg138.tsbook.setting.BookConfig.language
import java.lang.IllegalArgumentException

enum class ItemType {
    UNKNOWN,
    BOOTS,
    RING,
    NECKLACE,
    BRACELET,
    CHESTPLATE,
    GLOVE,
    HELMET,
    LEGGINGS,
    WINGS,
    BOOK;

    override fun toString(): String {
        return language.attribute.item.type[this]
            ?: throw IllegalArgumentException("No name set for ${this.name} in the language file!")
    }

    companion object {
        fun of(string: String): ItemType? {
            return try {
                valueOf(string)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}