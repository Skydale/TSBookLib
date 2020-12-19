package io.github.twilight_book.items;

import java.util.Random;

public class DamageRange {
    final double max;
    final double min;

    public DamageRange(double max, double min) {
        if (max > min) {
            this.max = max;
            this.min = min;
        } else {
            this.max = min;
            this.min = max;
        }
    }

    public double calculate(){
        return new Random().nextDouble() * (max - min) + min;
    }
}
