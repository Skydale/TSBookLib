package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.Translate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class ItemTemplate {
	final Config CONFIG;
	final String ID;

	YamlConfiguration SETTING;
	ItemStack ITEM;
	ItemMeta META;

	public ItemTemplate(Material m, Config config, String id){
		CONFIG = config;
		ID = id;
		SETTING = CONFIG.getItemByID(ID);

		ITEM = createItem(m);
	}

	public Config getConfig() { return CONFIG; }
	public ItemStack getItem() {
		return ITEM;
	}

	public String getID(){ return ID; }
	public String getDataID(String k, JavaPlugin p){
		return getDataID(ITEM, k, p);
	}

	public ItemStack createItem(Material m){
		ItemStack item = new ItemStack(m);
		item.setItemMeta(CONFIG.getLang().translateItem(this, item));

		return item;
	}

	public static String getDataID(ItemStack i, String k, JavaPlugin p) {
		String result = "";
		NamespacedKey key = new NamespacedKey(p, k);
		PersistentDataContainer container;

		container = i.getItemMeta().getPersistentDataContainer();

		if(container.has(key, PersistentDataType.STRING)){
			result = container.get(key, PersistentDataType.STRING);
		}

		return result;
	}

	protected void setDataID(String s, String k, JavaPlugin p) {
		NamespacedKey key = new NamespacedKey(p, k);

		META.getPersistentDataContainer().set(key, PersistentDataType.STRING, s);
		ITEM.setItemMeta(META);
	}

	public YamlConfiguration getSetting(){
		return SETTING;
	}

	public static ItemStack createGuiItem(Material material, String name, String... lore) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));

		item.setItemMeta(meta);

		return item;
	}
}