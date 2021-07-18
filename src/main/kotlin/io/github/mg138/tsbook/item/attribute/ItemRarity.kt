package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.config.BookConfig.language

enum class ItemRarity {
    UNKNOWN,
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    MYTHIC;

    override fun toString(): String {
        return language.attribute.item.rarity.names[this]
            ?: throw IllegalArgumentException("No name set for ${this.name} in the language file!")
    }
}