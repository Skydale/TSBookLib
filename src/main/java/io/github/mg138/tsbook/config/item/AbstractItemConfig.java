package io.github.mg138.tsbook.config.item;

import io.github.mg138.tsbook.config.gui.ArmorGUIConfig;
import io.github.mg138.tsbook.config.item.element.ItemSetting;
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
}
