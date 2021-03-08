package io.github.mg138.tsbook.utils.config.item;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractItemConfig {
    final Map<String, ItemSetting> items = new HashMap<>();

    public void unload() { items.clear(); }

    public Set<String> getItems() {
        return items.keySet();
    }

    public ItemSetting getItemByID(String ID) {
        return items.get(ID);
    }

    public AbstractItemConfig(Map<String, YamlConfiguration> items) {
        items.forEach((string, setting) -> this.items.put(string, new ItemSetting(setting)));
    }
}
