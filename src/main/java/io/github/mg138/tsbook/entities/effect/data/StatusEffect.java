package io.github.mg138.tsbook.entities.effect.data;

public class StatusEffect {
    public final StatusEffectType type;
    public final double power;
    public final int ticks;

    public StatusEffect(StatusEffectType type, double power, int ticks) {
        this.type = type;
        this.power = power;
        this.ticks = ticks;
    }
}
