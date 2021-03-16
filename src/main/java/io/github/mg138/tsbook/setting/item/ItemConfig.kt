package io.github.mg138.tsbook.setting.item;

import io.github.mg138.tsbook.setting.item.element.ItemSetting;
import io.github.mg138.tsbook.setting.item.element.SimpleItemSetting;
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting;
import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemConfig extends AbstractItemConfig {
    private static final ItemConfig instance = new ItemConfig();

    public static ItemConfig getInstance() {
        return instance;
    }

    private ItemConfig() {
    }

    final Map<String, UnidentifiedSetting> unid = new HashMap<>();

    public void load(Map<String, YamlConfiguration> items, Map<String, YamlConfiguration> unid) {
        items.forEach((string, setting) -> {
            if (setting.contains("stats")) {
                this.items.put(string, new StatedItemSetting(setting));
            } else {
                this.items.put(string, new SimpleItemSetting(setting));
            }
        });
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
