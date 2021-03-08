package io.github.mg138.tsbook.utils.config.item;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.Set;

public class ItemConfig extends AbstractItemConfig {
    final Map<String, UnidentifiedSetting> unid;

    public ItemConfig(Map<String, YamlConfiguration> items, Map<String, YamlConfiguration> unid) {
        super(items);
        unid.forEach((string, setting) -> this.items.put(string, new UnidentifiedSetting(setting)));
    }

    public Set<String> getUnidentified() {
        return unid.keySet();
    }

    public UnidentifiedSetting getUnidentifiedByID(String ID) {
        return unid.get(ID);
    }

    public ItemSetting getAnyItemByID(String ID) {
        if (items.containsKey(ID)) return getItemByID(ID);
        if (unid.containsKey(ID)) return getUnidentifiedByID(ID);
        return null;
    }
}
