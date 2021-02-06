package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.items.data.stat.StatType;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Random;

public class ItemIdentification {
    private final HashMap<StatType, Float> percentageMap = new HashMap<>();

    private ItemIdentification(ConfigurationSection stats, boolean isRandom) {
        if (isRandom) {
            Random random = new Random();

            for (String literalType : stats.getKeys(false)) {
                float percentage = (float) Math.round(100 * ((random.nextGaussian() / 4) + 0.5)) / 100;

                if (percentage < 0) percentage = 0;
                else if (percentage > 1) percentage = 1;

                percentageMap.put(StatType.valueOf(literalType.toUpperCase()), percentage);
            }
        } else {
            for (String literalType : stats.getKeys(false)) {
                percentageMap.put(StatType.valueOf(literalType.toUpperCase()), 1F);
            }
        }
    }

    public static ItemIdentification create(YamlConfiguration item, boolean random) {
        ConfigurationSection stats = item.getConfigurationSection("stat");
        if (stats == null) return null;

        return new ItemIdentification(stats, random);
    }

    public HashMap<StatType, Float> getPercentageMap() {
        return percentageMap;
    }

    public Float getStatPercentage(StatType statType) {
        getPercentageMap().putIfAbsent(statType, 0.5F);
        return getPercentageMap().get(statType);
    }
}