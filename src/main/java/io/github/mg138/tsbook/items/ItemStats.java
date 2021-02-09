package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.items.data.stat.map.RegisteredPlaceholder;
import io.github.mg138.tsbook.utils.config.AbstractConfig;
import io.github.mg138.tsbook.items.data.stat.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class ItemStats {
    private final ItemIdentification IDENTIFICATION;
    private final HashMap<StatType, StatMap> STATS = new HashMap<>();
    private final HashMap<String, String> PLACEHOLDER = new HashMap<>();
    private final AbstractConfig CONFIG;

    public ItemIdentification getIdentification() {
        return IDENTIFICATION;
    }

    public HashMap<String, String> getPlaceholders() {
        return PLACEHOLDER;
    }

    public HashMap<StatType, StatMap> getStats() {
        return STATS;
    }

    public Double getStat(StatType type) {
        StatMap statMap = STATS.get(type);
        if (statMap == null) return null;
        return statMap.getValue().getStat() * getIdentification().getStatPercentage(type);
    }

    public String translate(StatMap statMap) {
        String format = CONFIG.translate.translate("format." + statMap.getKey().toString());
        double percentage = IDENTIFICATION.getStatPercentage(statMap.getKey());

        Stat stat = statMap.getValue();
        if (stat instanceof StatRange) {
            StatRange statRange = (StatRange) stat;
            return format
                    .replace("[min]", String.valueOf((int) (statRange.getMin() * percentage)))
                    .replace("[max]", String.valueOf((int) (statRange.getMax() * percentage)))
                    .replace("[percentage]", String.valueOf((int) (percentage * 100)) + '%');
        } else {
            return format
                    .replace("[stat]", String.valueOf((int) (stat.getStat() * percentage)))
                    .replace("[percentage]", String.valueOf((int) (percentage * 100)) + '%');
        }
    }

    public ItemStats(ConfigurationSection settings, ItemIdentification identification, AbstractConfig config) {
        CONFIG = config;
        IDENTIFICATION = identification;
        for (String literalType : settings.getKeys(false)) {
            StatType type = StatType.valueOf(literalType.toUpperCase());
            StatMap statMap;

            if (settings.contains(literalType + ".min")) {
                statMap = new StatMap(type, new StatRange(
                        settings.getDouble(literalType + ".max"), settings.getDouble(literalType + ".min"))
                );
            } else {
                statMap = new StatMap(type, new StatSingle(
                        settings.getDouble(literalType))
                );
            }

            STATS.put(type, statMap);
            PLACEHOLDER.put(RegisteredPlaceholder.HOLDER.get(type), translate(statMap));
        }
    }
}