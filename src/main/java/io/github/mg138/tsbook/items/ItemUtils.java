package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.items.data.tag.IdentificationTag;
import io.github.mg138.tsbook.items.data.tag.UUIDTag;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemUtils {
    private static final UUIDTag UUID_TAG_TYPE = new UUIDTag();
    private static final IdentificationTag IDENTIFICATION_TAG_TYPE = new IdentificationTag();
    private static final Map<UUID, ItemInstance> ITEMS = new HashMap<>();
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

    public static ItemStack createItem(JavaPlugin plugin, ItemInstance inst, String type) {
        ItemStack item = new ItemStack(inst.getMaterial(), 1);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        String ID = inst.getID();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        ITEMS.put(constructUUID(plugin, container), inst);
        container.set(new NamespacedKey(plugin, type), PersistentDataType.STRING, ID);
        //SET UUID

        String itemType = inst.getItemType();
        if (itemType != null) {
            container.set(new NamespacedKey(plugin, "type"), PersistentDataType.STRING, itemType);
            //SET ITEM TYPE
        }

        Integer model = inst.getModel();
        if (model != null) {
            meta.setCustomModelData(model);
        }

        item.setItemMeta(meta);
        setIdentification(inst, item);
        return item;
    }

    public static ItemInstance getInstByItem(JavaPlugin plugin, ItemStack item, String type) {
        if (item.getType() == Material.AIR) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        UUID uuid = constructUUID(plugin, container);

        ItemInstance inst = ITEMS.get(uuid);

        if (inst == null) {
            String ID = container.get(new NamespacedKey(plugin, type), PersistentDataType.STRING);
            if (ID == null) return null;

            inst = new ItemInstance(Book.getCfg(), Book.getCfg().getItemConfig().getAnyItemByID(ID), getIdentification(plugin, item));
            ITEMS.put(uuid, inst);
        }

        return inst;
    }

    public static void setIdentification(ItemInstance inst, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        ItemStats stats = inst.getStats();
        if (stats == null) return;

        ItemIdentification identification = stats.getIdentification();
        if (identification == null) return;

        meta.getPersistentDataContainer().set(new NamespacedKey(Book.getInst(), "iden"), IDENTIFICATION_TAG_TYPE, identification);
        item.setItemMeta(meta);
    }

    public static ItemIdentification getIdentification(JavaPlugin plugin, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "iden"), IDENTIFICATION_TAG_TYPE);
    }

    public static String getDataTag(JavaPlugin plugin, ItemStack item, String k) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, k), PersistentDataType.STRING);
    }

    public static String getItemType(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        final String[] ID = {null};
        meta.getPersistentDataContainer().getKeys().forEach(
                key -> {
                    if (key.getNamespace().equals("tsbooklib")) {
                        if (REGISTERED_TAG.contains(key.getKey())) {
                            ID[0] = key.getKey();
                        }
                    }
                }
        );

        return ID[0];
    }

    public static UUID getUUID(ItemStack item, JavaPlugin plugin) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "uuid"), UUID_TAG_TYPE);
    }

    public static boolean hasItemID(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        Set<NamespacedKey> data = meta.getPersistentDataContainer().getKeys();

        for (NamespacedKey key : data) {
            if (key.getKey().equals("item")) {
                return true;
            }
        }
        return false;
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
