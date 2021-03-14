package io.github.mg138.tsbook.items.data.stat.set;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumSet;

public class EffectPowerType {
    public static final EnumSet<StatType> EFFECT_POWER = EnumSet.of(
            StatType.POWER_DODGE,
            StatType.POWER_DRAIN,
            StatType.POWER_LEVITATION,
            StatType.POWER_POISON,
            StatType.POWER_NAUSEOUS,
            StatType.POWER_REFLECT,
            StatType.POWER_SLOWNESS
    );
}