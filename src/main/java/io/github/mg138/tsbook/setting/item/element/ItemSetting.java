package io.github.mg138.tsbook.setting.item.element;

import org.bukkit.Material;

import java.util.List;

public abstract class ItemSetting {
    public final String ID;
    public final String ITEM_TYPE;
    public final Material MATERIAL;
    public final Integer MODEL;
    public final String NAME;
    public final List<String> LORE;

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
