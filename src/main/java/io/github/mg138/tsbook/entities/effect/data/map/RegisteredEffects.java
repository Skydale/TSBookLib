package io.github.mg138.tsbook.entities.effect.data.map;

import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import io.github.mg138.tsbook.entities.effect.data.effect.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Consumer;

public class RegisteredEffects {
    @NotNull
    private static EnumMap<StatusEffectType, Consumer<EntityStatusEffect>> constructEffects() {
        EnumMap<StatusEffectType, Consumer<EntityStatusEffect>> effects = new EnumMap<>(StatusEffectType.class);
        effects.put(StatusEffectType.BURNING, Burning.burning);
        effects.put(StatusEffectType.BLEEDING, Bleeding.bleeding);
        effects.put(StatusEffectType.FALL_DAMAGE_RESISTANCE, FallDamageResistance.fallDamageResistance);
        effects.put(StatusEffectType.SLOWNESS, Slowness.slowness);
        effects.put(StatusEffectType.LEVITATION, Levitation.levitation);
        effects.put(StatusEffectType.NAUSEOUS, Nauseous.nauseous);
        effects.put(StatusEffectType.PARALYSIS, Paralysis.paralysis);
        return effects;
    }

    public static final EnumMap<StatusEffectType, Consumer<EntityStatusEffect>> Effects = constructEffects();
}