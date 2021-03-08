package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.utils.config.item.ItemSetting;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

public class ItemInstance { //represents a single ItemStack
    private final ItemSetting SETTING;
    private final ItemStats STATS;
    private final String INTERNAL_TYPE;
    private final UUID uuid;

    public ItemStack createItem(JavaPlugin plugin) {
        return ItemUtils.createItem(plugin, this);
    }

    public ItemInstance(ItemSetting setting, ItemStats stats, String internalType, UUID uuid) {
        this.SETTING = setting;
        this.INTERNAL_TYPE = internalType;
        this.uuid = uuid;
        this.STATS = stats;

        if (this.STATS != null) putStatsInLore();
    }

    public void putStatsInLore() {
        ListIterator<String> iterator = SETTING.LORE.listIterator();
        Map<String, String> placeholders = STATS.getPlaceholders();
        while (iterator.hasNext()) {
            String s = iterator.next();
            placeholders.forEach(
                    (placeholder, stat) -> {
                        if (s.contains(placeholder)) iterator.set(s.replace(placeholder, stat));
                    }
            );
        }
    }

    public String getName() {
        return SETTING.NAME;
    }

    public List<String> getLore() {
        return SETTING.LORE;
    }

    public String getID() {
        return SETTING.ID;
    }

    public String getItemType() {
        return SETTING.ITEM_TYPE;
    }

    public String getInternalType() {
        return INTERNAL_TYPE;
    }

    public Material getMaterial() {
        return SETTING.MATERIAL;
    }

    public Integer getModel() {
        return SETTING.MODEL;
    }

    public ItemStats getStats() {
        return STATS;
    }

    public UUID getUUID() {
        return uuid;
    }
}