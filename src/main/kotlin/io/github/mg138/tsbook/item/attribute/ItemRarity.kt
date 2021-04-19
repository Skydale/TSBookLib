package io.github.mg138.tsbook.item.attribute

import io.github.mg138.tsbook.setting.BookConfig.language

enum class ItemRarity {
    UNKNOWN,
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    MYTHIC;

    override fun toString(): String {
        return language.attribute.item.rarity[this]
            ?: throw IllegalArgumentException("No name set for ${this.name} in the language file!")
    }
}