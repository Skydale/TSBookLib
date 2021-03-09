package io.github.mg138.tsbook.config.item.element;

import io.github.mg138.tsbook.Book;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

/*
    Declares the most basic instance of a ItemSetting
 */

public class SimpleItemSetting extends ItemSetting {
    public SimpleItemSetting(YamlConfiguration setting) {
        this(
                setting.getString("ID"),
                setting.getString("ITEM_TYPE"),
                Material.valueOf(setting.getString("MATERIAL")),
                setting.getInt("MODEL"),
                Book.Companion.getCfg().translate.translateString(setting.getString("NAME")),
                Book.Companion.getCfg().translate.translateByList(setting.getStringList("LORE"))
        );
    }

    public SimpleItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore) {
        super(id, item_type, material, model, name, lore);
    }
}
