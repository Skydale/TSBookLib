package io.github.mg138.tsbook.stat.util.set

import io.github.mg138.tsbook.stat.StatType
import java.util.*

object ElementDamageType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.DAMAGE_TEMPUS,
        StatType.DAMAGE_AQUA,
        StatType.DAMAGE_IGNIS,
        StatType.DAMAGE_LUMEN,
        StatType.DAMAGE_TERRA,
        StatType.DAMAGE_UMBRA
    )
}