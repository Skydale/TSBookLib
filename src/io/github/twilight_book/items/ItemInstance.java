package io.github.twilight_book.items;

import io.github.twilight_book.Book;
import io.github.twilight_book.utils.itemTag.UUIDTagType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.SerializationUtils;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemInstance { //represents a single ItemStack
    private static Map<UUID,ItemInstance> instances = new HashMap<>();
    private static PersistentDataType<byte[],UUID> uuidTagType = new UUIDTagType();
    private static NamespacedKey idKey = new NamespacedKey(Book.getInst(),"item");

    private String displayName;
    private List<String> lore;
    private YamlConfiguration configuration;
    private ItemStack item;

    public static ItemInstance getFromExist(ItemStack i){
        if(i.getType() == Material.AIR) return null;
        PersistentDataContainer container = Objects.requireNonNull(i.getItemMeta()).getPersistentDataContainer();
        String itemId = container.get(idKey,PersistentDataType.STRING);
        Bukkit.broadcastMessage(itemId+"");
        if(itemId == null)return null;
        UUID uuid = getOrGenerateUUID(container);
        if(instances.containsKey(uuid)) return instances.get(uuid);
        else {
            ItemInstance i2 = new ItemInstance(itemId);
            instances.put(uuid,i2);
            i2.item = i;
            return i2;
        }
    }

    public ItemInstance(String itemId){
        configuration = Book.getCfg().getItemByID(itemId);
        displayName = Book.getCfg().getLang().translate("FORMAT.NAME", null, configuration);
        lore = Book.getCfg().getLang().translateList("FORMAT.LORE", null, configuration);
    }

    public ItemStack createItem(){
        if(item!=null)return item;
        Material m = Material.getMaterial(configuration.getString("MATERIAL"));
        item = new ItemStack((m==null)?Material.STONE:m,1);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        instances.put(getOrGenerateUUID(container),this);
        container.set(idKey,PersistentDataType.STRING, configuration.getString("ID"));
        item.setItemMeta(meta);
        return item;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    private ItemInstance(String itemId, List<String> lore) {//TODO identification also unused
        this.lore = lore;
    }

    private static UUID getOrGenerateUUID(PersistentDataContainer container){
        NamespacedKey uuidk = new NamespacedKey(Book.getInst(),"uuid");
        UUID uuid = container.get(uuidk, uuidTagType);
        if(uuid == null){
            uuid = UUID.randomUUID();
            container.set(uuidk,uuidTagType,uuid);
        }
        return uuid;
    }

}
