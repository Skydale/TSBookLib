package io.github.mg138.tsbook.items.data;

import java.io.Serializable;
import java.util.Random;

public class StatRange implements Serializable {
    public final double max;
    public final double min;

    public StatRange(double max, double min) {
        if (max > min) {
            this.max = max;
            this.min = min;
        } else {
            this.max = min;
            this.min = max;
        }
    }

    public double calculate(){
        if (max == min) return max;
        return calculate(new Random().nextDouble());
    }

    public double calculate(double percentage) {
        if (max == min) return max;
        return ((percentage) * (max - min)) + min;
    }
}
