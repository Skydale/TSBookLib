package io.github.mg138.tsbook.items.data.stat;

import java.util.Random;

public class StatRange implements Stat {
    private final double max;
    private final double min;

    public StatRange(double max, double min) {
        if (max > min) {
            this.max = max;
            this.min = min;
        } else {
            this.max = min;
            this.min = max;
        }
    }

    public double getStat() {
        if (max == min) return max;
        return getStat(new Random().nextDouble());
    }

    public double getStat(double percentage) {
        if (max == min) return max;
        return ((percentage) * (max - min)) + min;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}
