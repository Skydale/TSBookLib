package io.github.mg138.tsbook.stat.util.set

import java.util.EnumSet
import io.github.mg138.tsbook.stat.StatType

object DefenseType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.DEFENSE_PHYSICAL,
        StatType.DEFENSE_TEMPUS,
        StatType.DEFENSE_AQUA,
        StatType.DEFENSE_TERRA,
        StatType.DEFENSE_IGNIS,
        StatType.DEFENSE_UMBRA,
        StatType.DEFENSE_LUMEN,
        StatType.DEFENSE_TRUE,
        StatType.DEFENSE_THUNDER
    )
}