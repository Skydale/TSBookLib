package io.github.mg138.tsbook.items.data;

import java.io.Serializable;

public class Stat implements Serializable {
    public StatRange statRange;
    public double stat;

    public Stat(double stat) {
        this.stat = stat;
    }

    public Stat(StatRange statRange) {
        this.statRange = statRange;
    }
}
