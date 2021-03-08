package io.github.mg138.tsbook.utils.config.item;

import io.github.mg138.tsbook.Book;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ItemSetting {
    public final String ID;
    public final String ITEM_TYPE;
    public final Material MATERIAL;
    public final Integer MODEL;
    public final String NAME;
    public final List<String> LORE;

    public ItemSetting(YamlConfiguration setting) {
        this(
                setting.getString("ID"),
                setting.getString("ITEM_TYPE"),
                Material.valueOf(setting.getString("MATERIAL")),
                setting.getInt("MODEL"),
                Book.Companion.getCfg().translate.translateString(setting.getString("NAME")),
                Book.Companion.getCfg().translate.translateByList(setting.getStringList("LORE"))
        );
    }

    public ItemSetting(ItemSetting setting) {
        this(
                setting.ID,
                setting.ITEM_TYPE,
                setting.MATERIAL,
                setting.MODEL,
                setting.NAME,
                setting.LORE
        );
    }

    public ItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore) {
        ID = id;
        ITEM_TYPE = item_type;
        MATERIAL = material;
        MODEL = model;
        NAME = name;
        LORE = lore;
    }
}
