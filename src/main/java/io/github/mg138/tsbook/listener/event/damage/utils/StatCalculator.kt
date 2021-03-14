package io.github.mg138.tsbook.listener.event.damage.utils;

public class StatCalculator {
    public static double calculateModifier(double stat, double modifier) {
        if (modifier > 0) {
            stat *= modifier;
        } else {
            modifier *= -1;
            stat *= (1 - (modifier / (modifier + 2)));
        }

        return stat;
    }

    public static double calculateTrueDamage(double damage, double defense, double modifier) {
        damage = calculateModifier(damage, modifier);

        damage -= defense;
        return damage < 0 ? 0 : damage;
    }

    public static double calculateDamage(double damage, double defense, double modifier) {
        damage = calculateModifier(damage, modifier);

        if (defense > 0) {
            damage *= (1 - (defense / (defense + 100)));
        } else if (defense < 0) {
            defense *= -1;
            damage *= (1 + (defense / (defense + 100)));
        }

        return damage;
    }
}
