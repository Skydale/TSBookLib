package io.github.mg138.tsbook.items.data.stat.set;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumSet;

public class ModifierType {
    public static final EnumSet<StatType> MODIFIER = EnumSet.of(
            StatType.MODIFIER_HELL,
            StatType.MODIFIER_MOBS,
            StatType.MODIFIER_PLAYER,
            StatType.MODIFIER_ARTHROPOD,
            StatType.MODIFIER_UNDEAD,
            StatType.MODIFIER_UNDERWATER
    );
}