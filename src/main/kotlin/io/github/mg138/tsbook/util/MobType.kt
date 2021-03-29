package io.github.mg138.tsbook.util

import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*

object MobType {
    fun isHellish(type: EntityType): Boolean {
        return when (type) {
            PIGLIN, PIGLIN_BRUTE,
            ZOMBIFIED_PIGLIN, ZOGLIN,
            MAGMA_CUBE, BLAZE, GHAST,
            HOGLIN, STRIDER, WITHER,
            WITHER_SKELETON, WITHER_SKULL -> true
            else -> false
        }
    }

    fun isMob(type: EntityType): Boolean {
        return when (type) {
            PIGLIN, PIGLIN_BRUTE,
            ZOMBIFIED_PIGLIN, ZOGLIN,
            MAGMA_CUBE, BLAZE,
            GHAST, HOGLIN, STRIDER,
            WITHER, WITHER_SKELETON,
            WITHER_SKULL, SPIDER, CAVE_SPIDER,
            ENDER_DRAGON, ENDERMAN, ENDERMITE,
            ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER,
            CREEPER, DROWNED, ELDER_GUARDIAN,
            EVOKER, HUSK, PHANTOM,
            PILLAGER, RAVAGER, SHULKER,
            SILVERFISH, SLIME, STRAY,
            VEX, VINDICATOR, WITCH, GIANT,
            GUARDIAN, ILLUSIONER -> true
            else -> false
        }
    }

    fun isArthropod(type: EntityType): Boolean {
        return when (type) {
            SPIDER, CAVE_SPIDER,
            BEE, ENDERMITE, SILVERFISH -> true
            else -> false
        }
    }

    fun isWatery(type: EntityType): Boolean {
        return when (type) {
            SQUID, DOLPHIN,
            GUARDIAN, ELDER_GUARDIAN,
            TURTLE, COD, SALMON,
            PUFFERFISH, TROPICAL_FISH -> true
            else -> false
        }
    }

    fun isUndead(type: EntityType): Boolean {
        return when (type) {
            ZOMBIE, DROWNED, HUSK,
            PHANTOM, SKELETON, SKELETON_HORSE,
            STRAY, WITHER, WITHER_SKELETON,
            ZOGLIN, ZOMBIE_HORSE,
            ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN -> true
            else -> false
        }
    }
}