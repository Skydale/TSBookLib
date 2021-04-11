package io.github.mg138.tsbook.attribute

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