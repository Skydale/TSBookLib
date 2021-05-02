package io.github.mg138.tsbook.item.attribute.stat.util

import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.item.attribute.stat.data.StatType.*
import java.util.*

object StatTypes {
    val damages: EnumSet<StatType> = EnumSet.of(
        DAMAGE_PHYSICAL,
        DAMAGE_TEMPUS,
        DAMAGE_AQUA,
        DAMAGE_TERRA,
        DAMAGE_IGNIS,
        DAMAGE_UMBRA,
        DAMAGE_LUMEN,
        DAMAGE_TRUE,
        DAMAGE_NONE,
        DAMAGE_BLEED,
        DAMAGE_THUNDER
    )

    val defenses: EnumSet<StatType> = EnumSet.of(
        DEFENSE_PHYSICAL,
        DEFENSE_TEMPUS,
        DEFENSE_AQUA,
        DEFENSE_TERRA,
        DEFENSE_IGNIS,
        DEFENSE_UMBRA,
        DEFENSE_LUMEN,
        DEFENSE_TRUE,
        DEFENSE_THUNDER
    )

    val effectChances: EnumSet<StatType> = EnumSet.of(
        CHANCE_DODGE,
        CHANCE_DRAIN,
        CHANCE_LEVITATION,
        CHANCE_POISON,
        CHANCE_NAUSEOUS,
        CHANCE_REFLECT,
        CHANCE_SLOWNESS
    )

    val effectPowers: EnumSet<StatType> = EnumSet.of(
        POWER_DODGE,
        POWER_DRAIN,
        POWER_LEVITATION,
        POWER_POISON,
        POWER_NAUSEOUS,
        POWER_REFLECT,
        POWER_SLOWNESS
    )

    val elementalDamages: EnumSet<StatType> = EnumSet.of(
        DAMAGE_PHYSICAL,
        DAMAGE_TEMPUS,
        DAMAGE_AQUA,
        DAMAGE_IGNIS,
        DAMAGE_LUMEN,
        DAMAGE_TERRA,
        DAMAGE_UMBRA
    )

    val modifiers: EnumSet<StatType> = EnumSet.of(
        MODIFIER_HELL,
        MODIFIER_MOBS,
        MODIFIER_PLAYER,
        MODIFIER_ARTHROPOD,
        MODIFIER_UNDEAD,
        MODIFIER_UNDERWATER
    )
}