package io.github.mg138.tsbook.material.util

import org.bukkit.Material

object MaterialUtil {
    fun isArmor(material: Material) = when (material) {
        Material.TURTLE_HELMET,
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.GOLDEN_HELMET,
        Material.IRON_HELMET,
        Material.DIAMOND_HELMET,
        Material.NETHERITE_HELMET,
        Material.LEATHER_CHESTPLATE,
        Material.CHAINMAIL_CHESTPLATE,
        Material.GOLDEN_CHESTPLATE,
        Material.IRON_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE,
        Material.NETHERITE_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.CHAINMAIL_LEGGINGS,
        Material.GOLDEN_LEGGINGS,
        Material.IRON_LEGGINGS,
        Material.DIAMOND_LEGGINGS,
        Material.NETHERITE_LEGGINGS,
        Material.LEATHER_BOOTS,
        Material.CHAINMAIL_BOOTS,
        Material.GOLDEN_BOOTS,
        Material.IRON_BOOTS,
        Material.DIAMOND_BOOTS,
        Material.NETHERITE_BOOTS -> true
        else -> false
    }
}