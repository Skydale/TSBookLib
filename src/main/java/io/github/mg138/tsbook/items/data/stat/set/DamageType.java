package io.github.mg138.tsbook.items.data.stat.set;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumSet;

public class DamageType {
    public static final EnumSet<StatType> DAMAGES = EnumSet.of(
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
    );
}