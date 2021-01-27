package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.utils.config.AbstractConfig;
import io.github.mg138.tsbook.items.data.stat.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class ItemStats {
    private final ItemIdentification IDENTIFICATION;
    private final HashSet<StatMap> STATS = new HashSet<>();
    private final HashMap<String, String> PLACEHOLDER = new HashMap<>();
    private final AbstractConfig CONFIG;

    public ItemIdentification getIdentification() {
        return IDENTIFICATION;
    }

    public HashMap<String, String> getPlaceholders() {
        return PLACEHOLDER;
    }

    public HashSet<StatMap> getStats() {
        return STATS;
    }

    public Stat getStat(StatType type) {
        Optional<StatMap> statMap = STATS.stream().filter((map) -> type.equals(map.getKey())).findAny();
        return statMap.map(StatMap::getValue).orElse(null);
    }

    public String translate(StatMap statMap) {
        String format = CONFIG.getTranslate().translate("format." + statMap.getKey().toString());
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
                        settings.getDouble(literalType + ".max"), settings.getDouble(literalType + ".min")
                ));
            } else {
                statMap = new StatMap(type, new StatSingle(
                        settings.getDouble(literalType)
                ));
            }

            STATS.add(statMap);
            PLACEHOLDER.put(ItemUtils.PLACEHOLDER.get(type), translate(statMap));
        }
    }
}