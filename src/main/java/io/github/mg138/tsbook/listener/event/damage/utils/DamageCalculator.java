package io.github.mg138.tsbook.listener.event.damage.utils;

public class DamageCalculator {
    public static double calculateTrueDamage(double damage, double defense, double modifier) {
        damage *= modifier;
        damage -= defense;
        return damage < 0 ? 0 : damage;
    }

    public static double calculateDamage(double damage, double defense, double modifier) {
        if (modifier > 0) {
            damage *= modifier;
        } else {
            modifier *= -1;
            damage *= (1 - (modifier / (modifier + 1)));
        }

        if (defense > 0) {
            damage *= (1 - (defense / (defense + 100)));
        } else if (defense < 0) {
            defense *= -1;
            damage *= (1 + (defense / (defense + 100)));
        }

        return damage;
    }
}
