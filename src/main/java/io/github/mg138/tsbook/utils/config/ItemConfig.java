package io.github.mg138.tsbook.utils.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.Set;

public class ItemConfig extends AbstractItemConfig {
    final Map<String, YamlConfiguration> unid;

    public ItemConfig(Map<String, YamlConfiguration> items, Map<String, YamlConfiguration> unid) {
        super(items);
        this.unid = unid;
    }

    public Set<String> getUnidentified() {
        return unid.keySet();
    }

    public YamlConfiguration getUnidentifiedByID(String ID) {
        return unid.get(ID);
    }

    public YamlConfiguration getAnyItemByID(String ID) {
        if (items.containsKey(ID)) return getItemByID(ID);
        if (unid.containsKey(ID)) return getUnidentifiedByID(ID);
        return null;
    }
}
