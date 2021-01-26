package io.github.mg138.tsbook.items.data.stat;

import java.util.Map.Entry;

public class StatMap implements Entry<StatType, Stat> {
    private StatType statType;
    private Stat stat;

    public StatMap(StatType key, Stat value) {
        statType = key;
        stat = value;
    }

    @Override
    public StatType getKey() {
        return statType;
    }

    @Override
    public Stat getValue() {
        return stat;
    }

    @Override
    public Stat setValue(Stat value) {
        if (stat.equals(value)) return stat;

        final Stat oldStat = stat;
        stat = value;
        return oldStat;
    }

    public void set(StatType key, Stat value) {
        statType = key;
        stat = value;
    }
}
