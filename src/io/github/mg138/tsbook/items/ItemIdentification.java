package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.items.data.Stat;
import io.github.mg138.tsbook.items.data.StatRange;
import io.github.mg138.tsbook.items.data.StatType;
import io.github.mg138.tsbook.utils.config.ConfigAbstract;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class ItemIdentification implements Serializable {
    private final HashMap<StatType, Float> stat = new HashMap<>();

    private ItemIdentification(ConfigurationSection stats, boolean isRandom) {
        if (isRandom) {
            Random random = new Random();

            for (String statType : stats.getKeys(false)) {
                float percentage = (float) Math.round(100 * ((random.nextGaussian() / 4) + 0.5)) / 100;

                if (percentage < 0) percentage = 0;
                else if (percentage > 1) percentage = 1;

                stat.put(StatType.valueOf(statType.toUpperCase()), percentage);
            }
        } else {
            for (String statType : stats.getKeys(false)) {
                stat.put(StatType.valueOf(statType.toUpperCase()), 1F);
            }
        }
    }

    public static ItemIdentification create(YamlConfiguration item, boolean random) {
        if (!item.contains("stat")) return null;
        ConfigurationSection stats = item.getConfigurationSection("stat");
        if (stats == null) return null;

        return new ItemIdentification(stats, random);
    }


    public HashMap<StatType, Float> getStat() {
        return stat;
    }

    public Float getStatPercentage(@NonNull StatType stat) {
        return this.stat.get(stat);
    }

    public String getDesc(StatType type, ConfigAbstract config, StatRange range) {
        String format = config.getLang().translate("format." + type.toString());
        double percentage = getStatPercentage(type);

        return format
                .replace("[min]", String.valueOf((int) (range.min * percentage)))
                .replace("[max]", String.valueOf((int) (range.max * percentage)))
                .replace("[percentage]", String.valueOf((int) (percentage * 100)) + '%');
    }

    public String getDesc(StatType type, ConfigAbstract config, Stat stat) {
        String format = config.getLang().translate("format." + type.toString());
        double percentage = getStatPercentage(type);

        return format
                .replace("[stat]", String.valueOf((int) (stat.stat * percentage)))
                .replace("[percentage]", String.valueOf((int) (percentage * 100)) + '%');
    }
}