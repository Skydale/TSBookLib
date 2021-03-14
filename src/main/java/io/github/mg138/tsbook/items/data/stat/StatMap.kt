package io.github.mg138.tsbook.items.data.stat;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class StatMap extends HashMap<StatType, Stat> {
    public static StatMap from(ConfigurationSection setting) {
        if (setting == null) return null;

        StatMap stats = new StatMap();
        for (String literalType : setting.getKeys(false)) {
            StatType type = StatType.valueOf(literalType.toUpperCase());
            Stat stat;

            if (setting.contains(literalType + ".min")) {
                stat = new StatRange(
                        setting.getDouble(literalType + ".max"),
                        setting.getDouble(literalType + ".min")
                );
            } else {
                stat = new StatSingle(
                        setting.getDouble(literalType)
                );
            }
            stats.put(type, stat);
        }
        return stats;
    }
}
