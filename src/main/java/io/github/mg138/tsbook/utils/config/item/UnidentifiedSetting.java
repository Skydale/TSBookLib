package io.github.mg138.tsbook.utils.config.item;

import io.github.mg138.tsbook.Book;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class UnidentifiedSetting extends ItemSetting {
    public final List<String> iden;

    public UnidentifiedSetting(YamlConfiguration setting) {
        this(
                new ItemSetting(setting),
                setting.getStringList("ITEMS")
        );
    }

    public UnidentifiedSetting(ItemSetting setting, List<String> iden) {
        super(setting);
        this.iden = iden;
    }

    public UnidentifiedSetting(String id, String item_type, Material material, Integer model, String name, List<String> lore, List<String> iden) {
        super(id, item_type, material, model, name, lore);
        this.iden = iden;
    }
}
