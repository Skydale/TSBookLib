package io.github.mg138.tsbook.config.item.element;

import io.github.mg138.tsbook.items.data.stat.StatMap;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatedItemSetting extends ItemSetting {
    public StatMap STATS = null;

    public StatedItemSetting(YamlConfiguration setting) {
        this (
                new SimpleItemSetting(setting),
                StatMap.from(setting.getConfigurationSection("stats"))
        );
    }

    public StatedItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore, StatMap stats) {
        this(new SimpleItemSetting(id, item_type, material, model, name, lore), stats);
    }

    public StatedItemSetting(ItemSetting setting, StatMap stats) {
        super(setting);
        if (stats != null) STATS = stats;
    }
}
