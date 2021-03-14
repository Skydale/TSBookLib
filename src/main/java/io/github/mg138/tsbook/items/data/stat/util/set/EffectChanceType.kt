package io.github.mg138.tsbook.items.data.stat.util.set

import java.util.EnumSet
import io.github.mg138.tsbook.items.data.stat.StatType

object EffectChanceType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.CHANCE_DODGE,
        StatType.CHANCE_DRAIN,
        StatType.CHANCE_LEVITATION,
        StatType.CHANCE_POISON,
        StatType.CHANCE_NAUSEOUS,
        StatType.CHANCE_REFLECT,
        StatType.CHANCE_SLOWNESS
    )
}