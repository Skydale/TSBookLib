package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemInstance { //represents a single ItemStack
    private final List<String> LORE;
    private String NAME;
    private YamlConfiguration CONFIG;
    private String PATH;

    public ItemInstance(ConfigAbstract config, String ID, String path) {
        switch (path.toLowerCase()) {
            case "item":
                CONFIG = config.getItemByID(ID);
                break;
            case "unid":
                CONFIG = config.getUnidentifiedByID(ID);
                break;
        }
        NAME = config.getLang().translate    ("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);
        PATH = path;
    }

    private ItemInstance(String ID, List<String> lore) {//TODO identification also unused
        LORE = lore;
    }

    public String getName() {
        return NAME;
    }

    public List<String> getLore() {
        return LORE;
    }

    public YamlConfiguration getConfig(){
        return CONFIG;
    }

    public ItemStack createItem(JavaPlugin plugin) {
        return ItemUtils.createItem(plugin, this, PATH);
    }
}