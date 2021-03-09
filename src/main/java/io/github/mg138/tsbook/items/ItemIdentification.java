package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.config.item.element.StatedItemSetting;
import io.github.mg138.tsbook.items.data.stat.StatType;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemIdentification {
    private final Map<StatType, Float> percentageMap = new HashMap<>();

    public ItemIdentification(StatedItemSetting setting, boolean isRandom) {
        if (isRandom) {
            Random random = new Random();

            setting.STATS.forEach((statType, stat) -> {
                float percentage = (float) Math.round(100 * ((random.nextGaussian() / 4) + 0.5)) / 100;

                if (percentage < 0) percentage = 0;
                else if (percentage > 1) percentage = 1;

                percentageMap.put(statType, percentage);
            });
        } else {
            setting.STATS.forEach((statType, stat) -> percentageMap.put(statType, 1F));
        }
    }

    public ItemIdentification(Map<StatType, Float> percentageMap) {
        this.percentageMap.putAll(percentageMap);
    }

    public Map<StatType, Float> getPercentageMap() {
        return percentageMap;
    }

    public Float getStatPercentage(StatType statType) {
        getPercentageMap().putIfAbsent(statType, 0.5F);
        return getPercentageMap().get(statType);
    }
}