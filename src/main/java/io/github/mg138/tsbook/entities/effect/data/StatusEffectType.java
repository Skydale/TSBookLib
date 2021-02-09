package io.github.mg138.tsbook.entities.effect.data;

import java.util.Arrays;
import java.util.List;

public enum StatusEffectType {
    BURNING,
    BLEEDING,
    FROZEN,
    FALL_DAMAGE_RESISTANCE,
    SLOWNESS,
    LEVITATION,
    NAUSEOUS,
    PARALYSIS;

    public static final List<String> names = Arrays.asList(Arrays.stream(values()).map(Enum::name).toArray(String[]::new));
}
