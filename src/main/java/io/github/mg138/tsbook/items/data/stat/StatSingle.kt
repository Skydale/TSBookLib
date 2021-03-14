package io.github.mg138.tsbook.items.data.stat;

public class StatSingle implements Stat {
    private final double stat;

    public StatSingle(double stat) {
        this.stat = stat;
    }

    public double getStat() {
        return stat;
    }
}
