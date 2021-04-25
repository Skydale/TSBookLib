package io.github.mg138.tsbook.item.attribute.stat

import io.github.mg138.tsbook.setting.BookConfig.language

enum class StatType {
    SPEED_ATTACK,
    DAMAGE_TRUE,
    DAMAGE_PHYSICAL,
    DAMAGE_TERRA,
    DAMAGE_TEMPUS,
    DAMAGE_IGNIS,
    DAMAGE_AQUA,
    DAMAGE_LUMEN,
    DAMAGE_UMBRA,
    DAMAGE_NONE,
    DAMAGE_BLEED,
    DAMAGE_THUNDER,
    CHANCE_CRITICAL,
    POWER_CRITICAL,
    MODIFIER_ARTHROPOD,
    MODIFIER_UNDEAD,
    MODIFIER_MOBS,
    MODIFIER_HELL,
    MODIFIER_UNDERWATER,
    MODIFIER_PLAYER,
    CHANCE_DRAIN,
    POWER_DRAIN,
    CHANCE_SLOWNESS,
    POWER_SLOWNESS,
    CHANCE_POISON,
    POWER_POISON,
    CHANCE_NAUSEOUS,
    POWER_NAUSEOUS,
    CHANCE_LEVITATION,
    POWER_LEVITATION,
    CHANCE_FROZE,
    DEFENSE_TRUE,
    DEFENSE_PHYSICAL,
    DEFENSE_TERRA,
    DEFENSE_TEMPUS,
    DEFENSE_IGNIS,
    DEFENSE_AQUA,
    DEFENSE_LUMEN,
    DEFENSE_UMBRA,
    CHANCE_REFLECT,
    POWER_REFLECT,
    CHANCE_DODGE,
    POWER_DODGE,
    DEFENSE_SPIDER,
    DEFENSE_UNDEAD,
    DEFENSE_MOBS,
    DEFENSE_HELL,
    DEFENSE_UNDERWATER,
    DEFENSE_PLAYER,
    DEFENSE_THUNDER,
    DEFENSE_SLOWNESS,
    DEFENSE_FIRE,
    DEFENSE_DRAIN,
    DEFENSE_LEVITATION,
    DEFENSE_FROZE,
    AFFINITY_ELEMENT;

    override fun toString() = language.attribute.stat.name[this]
        ?: throw IllegalArgumentException("No name set for ${this.name} in the language file!")

    fun getFormat() = language.attribute.stat.format[this]
        ?: throw IllegalArgumentException("No format set for ${this.name} in the language file!")

    fun getIndicator() = language.attribute.stat.indicator[this]
        ?: throw IllegalArgumentException("No indicator set for ${this.name} in the language file!")
}