package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class ItemIdentification implements Serializable {
    private final HashMap<ItemUtils.DamageType, Float> stat = new HashMap<>();

    public ItemIdentification(ItemInstance inst) {
        this(inst.getConfig(), true);
    }

    public ItemIdentification(ConfigAbstract config, String ID) {
        this(config.getItemByID(ID), true);
    }

    public ItemIdentification(YamlConfiguration item, boolean isRandom) {
        //TODO SUPPORT OTHER STATS
        if (!item.contains("stat.damage")) return;
        ConfigurationSection damage = item.getConfigurationSection("stat.damage");
        if (damage == null) return;

        if (isRandom) {
            Random random = new Random();

            for (String damageType : damage.getKeys(false)) {
                float percentage = (float) Math.round(100 * ((random.nextGaussian() / 4) + 0.5)) / 100;

                if (percentage < 0) percentage = 0;
                else if (percentage > 1) percentage = 1;

                setIdentification(damageType, percentage);
            }
        } else {
            for (String damageType : damage.getKeys(false)) {
                setIdentification(damageType, 1);
            }
        }
    }

    private void setIdentification(String damageType, float percentage) {
        stat.put(ItemUtils.DamageType.valueOf(damageType.toUpperCase()), percentage);
    }

    public HashMap<ItemUtils.DamageType, Float> getStat() {
        return stat;
    }

    public Float getStatPercentage(ItemUtils.@NonNull DamageType stat) {
        return this.stat.get(stat);
    }

    public String getDesc(ItemUtils.DamageType stat, ConfigAbstract config, StatRange range) {
        String format = config.getLang().translate("format.damage-range." + stat.toString().toLowerCase());
        double percentage = getStatPercentage(stat);
        return format.replaceAll("\\[min]",        String.valueOf((int) (range.min * percentage)))
                     .replaceAll("\\[max]",        String.valueOf((int) (range.max * percentage)))
                     .replaceAll("\\[percentage]", String.valueOf((int) (percentage * 100)) + '%');
    }
}