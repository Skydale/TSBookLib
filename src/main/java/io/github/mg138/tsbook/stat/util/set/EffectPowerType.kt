package io.github.mg138.tsbook.stat.util.set

import java.util.EnumSet
import io.github.mg138.tsbook.stat.StatType

object EffectPowerType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.POWER_DODGE,
        StatType.POWER_DRAIN,
        StatType.POWER_LEVITATION,
        StatType.POWER_POISON,
        StatType.POWER_NAUSEOUS,
        StatType.POWER_REFLECT,
        StatType.POWER_SLOWNESS
    )
}