package io.github.twilight_book.items;

import io.github.twilight_book.Book;
import io.github.twilight_book.utils.uuid.UUIDTag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemUtils {
    private static final Map<UUID, ItemInstance> ITEMS = new HashMap<>();
    private static final PersistentDataType<byte[], UUID> UUID_TAG_TYPE = new UUIDTag();
    private static final List<String> REGISTERED_TAG = Arrays.asList(
            "item",
            "unid"
    );

    public static UUID constructUUID(JavaPlugin plugin, PersistentDataContainer container) {
        NamespacedKey key = new NamespacedKey(plugin, "uuid");

        UUID uuid = container.get(key, UUID_TAG_TYPE);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            container.set(key, UUID_TAG_TYPE, uuid);
        }
        return uuid;
    }

    public static ItemStack createItem(JavaPlugin plugin, ItemInstance inst, String path) {
        String mat = inst.getConfig().getString("MATERIAL");
        if (mat == null) throw new IllegalArgumentException("Cannot get Material of the item.");

        Material material = Material.getMaterial(mat);

        ItemStack item = new ItemStack((material == null) ? Material.STONE : material, 1);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        PersistentDataContainer container = meta.getPersistentDataContainer();
        ITEMS.put(constructUUID(plugin, container), inst);

        String ID = inst.getConfig().getString("ID");
        if (ID == null) throw new IllegalArgumentException("Cannot get ID of the item.");

        container.set(new NamespacedKey(plugin, path), PersistentDataType.STRING, ID);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemInstance getItem(JavaPlugin plugin, ItemStack item, String path) {
        if (item.getType() == Material.AIR) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        String ID = container.get(new NamespacedKey(plugin, path), PersistentDataType.STRING);
        if (ID == null) return null;

        UUID UUID = constructUUID(plugin, container);
        if (ITEMS.containsKey(UUID)) {
            return ITEMS.get(UUID);
        } else {
            ItemInstance inst = new ItemInstance(Book.getCfg(), ID, path);
            ITEMS.put(UUID, inst);
            return inst;
        }
    }

    public static String getDataTag(JavaPlugin plugin, ItemStack item, String k) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, k), PersistentDataType.STRING);
    }

    public static String getItemID(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        final String[] ID = {null};
        meta.getPersistentDataContainer().getKeys().forEach(
                key -> {
                    if (REGISTERED_TAG.contains(key.getKey())) ID[0] = key.getKey();
                }
        );
        return ID[0];
    }

    public static void setDataTag(JavaPlugin plugin, ItemStack item, String k, String v) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, k), PersistentDataType.STRING, v);
        item.setItemMeta(meta);
    }

    public static void unload() {
        ITEMS.clear();
    }
}
