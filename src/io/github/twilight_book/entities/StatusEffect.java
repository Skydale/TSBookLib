package io.github.twilight_book.entities;

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