package io.github.mg138.tsbook.items.data.stat.util.set

import java.util.EnumSet
import io.github.mg138.tsbook.items.data.stat.StatType

object ModifierType {
    val types: EnumSet<StatType> = EnumSet.of(
        StatType.MODIFIER_HELL,
        StatType.MODIFIER_MOBS,
        StatType.MODIFIER_PLAYER,
        StatType.MODIFIER_ARTHROPOD,
        StatType.MODIFIER_UNDEAD,
        StatType.MODIFIER_UNDERWATER
    )
}