package io.github.mg138.tsbook.utils.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.Set;

public abstract class AbstractItemConfig {
    final Map<String, YamlConfiguration> items;

    public Set<String> getItems() {
        return items.keySet();
    }

    public YamlConfiguration getItemByID(String ID) {
        return items.get(ID);
    }

    public AbstractItemConfig(Map<String, YamlConfiguration> items) {
        this.items = items;
    }
}
