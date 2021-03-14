package io.github.mg138.tsbook.setting.item.element;

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
                Book.Companion.getSetting().translate.translate("FORMAT.NAME", null, setting),
                Book.Companion.getSetting().translate.translateList("FORMAT.LORE", null, setting)
        );
    }

    public SimpleItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore) {
        super(id, item_type, material, model, name, lore);
    }
}
