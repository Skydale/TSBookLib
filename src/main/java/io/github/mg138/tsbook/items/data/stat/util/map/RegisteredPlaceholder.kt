package io.github.mg138.tsbook.items.data.stat.util.map

import io.github.mg138.tsbook.items.data.stat.StatType
import java.util.*

object RegisteredPlaceholder {
    private fun constructPlaceholder(): EnumMap<StatType, String> {
        val map = EnumMap<StatType, String>(StatType::class.java)
        map[StatType.CHANCE_CRITICAL] = "[chance-critical]"
        map[StatType.POWER_CRITICAL] = "[power-critical]"
        map[StatType.DAMAGE_PHYSICAL] = "[damage-physical]"
        map[StatType.DAMAGE_IGNIS] = "[damage-ignis]"
        map[StatType.DAMAGE_TEMPUS] = "[damage-tempus]"
        map[StatType.DAMAGE_TERRA] = "[damage-terra]"
        map[StatType.DAMAGE_AQUA] = "[damage-aqua]"
        map[StatType.DAMAGE_LUMEN] = "[damage-lumen]"
        map[StatType.DAMAGE_UMBRA] = "[damage-umbra]"
        map[StatType.DAMAGE_TRUE] = "[damage-true]"
        map[StatType.DEFENSE_PHYSICAL] = "[defense-physical]"
        map[StatType.DEFENSE_IGNIS] = "[defense-ignis]"
        map[StatType.DEFENSE_TEMPUS] = "[defense-tempus]"
        map[StatType.DEFENSE_TERRA] = "[defense-terra]"
        map[StatType.DEFENSE_AQUA] = "[defense-aqua]"
        map[StatType.DEFENSE_LUMEN] = "[defense-lumen]"
        map[StatType.DEFENSE_UMBRA] = "[defense-umbra]"
        map[StatType.DEFENSE_TRUE] = "[defense-true]"
        map[StatType.MODIFIER_PLAYER] = "[modifier-player]"
        map[StatType.CHANCE_DRAIN] = "[chance-drain]"
        map[StatType.POWER_DRAIN] = "[power-drain]"
        map[StatType.CHANCE_SLOWNESS] = "[chance-slowness]"
        map[StatType.POWER_SLOWNESS] = "[power-slowness]"
        map[StatType.CHANCE_LEVITATION] = "[chance-levitation]"
        map[StatType.POWER_LEVITATION] = "[power-levitation]"
        map[StatType.CHANCE_NAUSEOUS] = "[chance-nauseous]"
        map[StatType.POWER_NAUSEOUS] = "[power-nauseous]"
        map[StatType.AFFINITY_ELEMENT] = "[affinity-element]"
        return map
    }

    val placeholders = constructPlaceholder()
}