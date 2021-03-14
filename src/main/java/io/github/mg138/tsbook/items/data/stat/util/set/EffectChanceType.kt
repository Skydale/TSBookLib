package io.github.mg138.tsbook.items.data.stat.set;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumSet;

public class EffectChanceType {
    public static final EnumSet<StatType> EFFECT_CHANCE = EnumSet.of(
            StatType.CHANCE_DODGE,
            StatType.CHANCE_DRAIN,
            StatType.CHANCE_LEVITATION,
            StatType.CHANCE_POISON,
            StatType.CHANCE_NAUSEOUS,
            StatType.CHANCE_REFLECT,
            StatType.CHANCE_SLOWNESS
    );
}