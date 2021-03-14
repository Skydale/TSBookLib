package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.config.item.element.ItemSetting;
import io.github.mg138.tsbook.config.item.element.StatedItemSetting;
import io.github.mg138.tsbook.config.AbstractConfig;
import io.github.mg138.tsbook.items.data.stat.*;

public class ItemStats {
    private final ItemIdentification identification;
    private final AbstractConfig config;
    private final StatMap stats;

    public Double getStat(StatType type) {
        return getStats().get(type).getStat() * getIdentification().getStatPercentage(type);
    }

    public StatMap getStats() {
        return stats;
    }

    public ItemIdentification getIdentification() {
        return identification;
    }

    public String translate(StatType type, Stat stat) {
        String format = config.translate.translate("format." + type.toString());
        double percentage = identification.getStatPercentage(type);

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

    public ItemStats(ItemIdentification identification, AbstractConfig config, StatMap stats) {
        this.identification = identification;
        this.config = config;
        this.stats = stats;
    }

    public static ItemStats create(ItemIdentification identification, AbstractConfig config, ItemSetting setting) {
        if (setting instanceof StatedItemSetting) {
            return new ItemStats(identification, config, ((StatedItemSetting) setting).STATS);
        } else return null;
    }

    public static ItemStats create(ItemIdentification identification, AbstractConfig config, String ID) {
        return create(
                identification,
                config,
                Book.Companion.getCfg().itemConfig.getAnyItemByID(ID)
        );
    }
}