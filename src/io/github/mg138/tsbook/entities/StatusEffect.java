package io.github.mg138.tsbook.entities;

public enum StatusEffect {
    BURNING,
    BLEEDING,
    FROZEN;

    public static StatusEffect getEffect(String s) {
        for (StatusEffect e : StatusEffect.values()) {
            if (e.name().equalsIgnoreCase(s)) {
                return e;
            }
        }
        return null;
    }
}