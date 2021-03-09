package io.github.mg138.tsbook.config.item.element;

import io.github.mg138.tsbook.Book;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public abstract class ItemSetting {
    public String ID;
    public String ITEM_TYPE;
    public Material MATERIAL;
    public Integer MODEL;
    public String NAME;
    public List<String> LORE;

    public ItemSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore) {
        ID = id;
        ITEM_TYPE = item_type;
        MATERIAL = material;
        MODEL = model;
        NAME = name;
        LORE = lore;
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
}
