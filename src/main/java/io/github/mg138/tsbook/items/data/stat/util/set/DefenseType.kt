package io.github.mg138.tsbook.items.data.stat.set;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumSet;

public class DefenseType {
    public static final EnumSet<StatType> DEFENSE = EnumSet.of(
            StatType.DEFENSE_PHYSICAL,
            StatType.DEFENSE_TEMPUS,
            StatType.DEFENSE_AQUA,
            StatType.DEFENSE_TERRA,
            StatType.DEFENSE_IGNIS,
            StatType.DEFENSE_UMBRA,
            StatType.DEFENSE_LUMEN,
            StatType.DEFENSE_TRUE,
            StatType.DEFENSE_THUNDER
    );
}