package io.github.mg138.tsbook.items.data.stat.util.map

import io.github.mg138.tsbook.items.data.stat.StatType
import java.util.*

object DamageDefenseRelation {
    private fun constructRelationship(): EnumMap<StatType, StatType> {
        val map = EnumMap<StatType, StatType>(StatType::class.java)
        map[StatType.DAMAGE_PHYSICAL] = StatType.DEFENSE_PHYSICAL
        map[StatType.DAMAGE_TEMPUS] = StatType.DEFENSE_TEMPUS
        map[StatType.DAMAGE_TERRA] = StatType.DEFENSE_TERRA
        map[StatType.DAMAGE_IGNIS] = StatType.DEFENSE_IGNIS
        map[StatType.DAMAGE_AQUA] = StatType.DEFENSE_AQUA
        map[StatType.DAMAGE_UMBRA] = StatType.DEFENSE_UMBRA
        map[StatType.DAMAGE_LUMEN] = StatType.DEFENSE_LUMEN
        map[StatType.DAMAGE_TRUE] = StatType.DEFENSE_TRUE
        return map
    }

    val relationship = constructRelationship()
}