package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.utils.config.AbstractConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class ItemInstance { //represents a single ItemStack
    private final String ID;
    private final String ITEM_TYPE;
    private final Material MATERIAL;
    private final Integer MODEL;
    private final String NAME;
    private final List<String> LORE;
    private final ItemStats STATS;

    public ItemInstance(AbstractConfig config, YamlConfiguration setting, ItemIdentification identification) {
        ID = setting.getString("ID");
        assert ID != null;

        NAME = config.translate.translate("FORMAT.NAME", null, setting);
        assert NAME != null;

        LORE = config.translate.translateList("FORMAT.LORE", null, setting);
        assert LORE != null;

        String material = setting.getString("MATERIAL");
        assert material != null;

        MATERIAL = Material.getMaterial(material);

        if (setting.contains("MODEL")) {
            MODEL = setting.getInt("MODEL");
        } else MODEL = null;

        if (setting.contains("ITEM_TYPE")) {
            ITEM_TYPE = setting.getString("ITEM_TYPE");
        } else ITEM_TYPE = null;

        if (setting.contains("stat")) {
            ConfigurationSection stats = setting.getConfigurationSection("stat");
            if (stats == null) throw new IllegalArgumentException("I cannot properly get Damage of the item");

            STATS = new ItemStats(stats, identification, config);
            putStatsInLore();
        } else STATS = null;
    }

    public String getName() {
        return NAME;
    }

    public List<String> getLore() {
        return LORE;
    }

    public String getID() {
        return ID;
    }

    public String getItemType() {
        return ITEM_TYPE;
    }

    public void putStatsInLore() {
        ListIterator<String> iterator = LORE.listIterator();
        HashMap<String, String> placeholders = STATS.getPlaceholders();
        while (iterator.hasNext()) {
            String s = iterator.next();
            placeholders.forEach(
                    (placeholder, stat) -> {
                        if (s.contains(placeholder)) iterator.set(s.replace(placeholder, stat));
                    }
            );
        }
    }

    public Material getMaterial() {
        return MATERIAL;
    }

    public Integer getModel() {
        return MODEL;
    }

    public ItemStats getStats() {
        return STATS;
    }

    public ItemStack createItem(JavaPlugin plugin, String path) {
        return ItemUtils.createItem(plugin, this, path);
    }
}