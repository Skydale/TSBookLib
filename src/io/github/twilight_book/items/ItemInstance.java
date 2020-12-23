package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemInstance { //represents a single ItemStack
    private final String NAME;
    private final List<String> LORE;
    private final String PATH;
    private final Material MATERIAL;
    private YamlConfiguration CONFIG;
    private int MODEL = 0;

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

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("MODEL")) {
            MODEL = CONFIG.getInt("MODEL");
        }
        PATH = path;
    }

    public ItemInstance(ConfigAbstract config, YamlConfiguration setting, String ID, String path) {
        CONFIG = setting;
        NAME = config.getLang().translate    ("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("MODEL")) {
            MODEL = CONFIG.getInt("MODEL");
        }
        PATH = path;
    }

    private ItemInstance(String ID, List<String> lore) { //TODO identification also unused
        NAME = null; //for using "final"
        PATH = null;
        CONFIG = null;
        MATERIAL = null;
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

    public int getModel() {
        return MODEL;
    }

    public Material getMaterial() {
        return MATERIAL;
    }

    public ItemStack createItem(JavaPlugin plugin) {
        return ItemUtils.createItem(plugin, this, PATH);
    }
}