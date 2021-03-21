package io.github.mg138.tsbook.stat.util.set

import io.github.mg138.tsbook.stat.StatType
import java.util.*

object DamageType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.DAMAGE_PHYSICAL,
        StatType.DAMAGE_TEMPUS,
        StatType.DAMAGE_AQUA,
        StatType.DAMAGE_TERRA,
        StatType.DAMAGE_IGNIS,
        StatType.DAMAGE_UMBRA,
        StatType.DAMAGE_LUMEN,
        StatType.DAMAGE_TRUE,
        StatType.DAMAGE_NONE,
        StatType.DAMAGE_BLEED,
        StatType.DAMAGE_THUNDER
    )
}