package io.github.mg138.tsbook.items.data.stat;

import java.util.Comparator;
import java.util.Map;

public class StatMap implements Map.Entry<StatType, Stat> {
    static class Sort implements Comparator<StatMap> {
        @Override
        public int compare(StatMap a, StatMap b) {
            return Double.compare(a.stat.getStat(), b.stat.getStat());
        }
    }

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
