package io.github.mg138.tsbook.utils.config.item;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class StatedItemSetting extends ItemSetting {


    public StatedItemSetting(YamlConfiguration setting) {
        super(setting);

    }

    public StatedItemSetting(ItemSetting setting) {
        super(setting);
    }

    public StatedItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore) {
        super(id, item_type, material, model, name, lore);
    }
}
