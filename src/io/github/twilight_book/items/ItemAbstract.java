package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.Translate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public abstract class ItemAbstract {
	final ItemStack ITEM;
	final Config CONFIG;
	final Translate LANG;
	ItemMeta META;

	public ItemAbstract(ItemStack i, Config config){
		ITEM = i;
		CONFIG = config;
		LANG = CONFIG.getLang();
		META = ITEM.getItemMeta();
	}
	public Config getConfig() { return CONFIG; }
	public ItemStack getItem() {
		return ITEM;
	}
	public ItemMeta getMeta() {
		return META;
	}
	public String getDataID(String k, JavaPlugin p){
		return getDataID(ITEM, k, p);
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

	protected void setMeta(String s){
		META = LANG.translateItem(this);
		ITEM.setItemMeta(META);
	}

	protected void setDataID(String s, String k, JavaPlugin p) {
		NamespacedKey key = new NamespacedKey(p, k);

		META.getPersistentDataContainer().set(key, PersistentDataType.STRING, s);
		ITEM.setItemMeta(META);
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