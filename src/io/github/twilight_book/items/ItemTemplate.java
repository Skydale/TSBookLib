package io.github.twilight_book.items;

import io.github.twilight_book.Book;
import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemTemplate {
	final ConfigAbstract CONFIG;
	final String ID;

	final YamlConfiguration SETTING;
	final ItemStack ITEM;
	ItemMeta META;

	public ItemTemplate(JavaPlugin plugin, Material m, ConfigAbstract config, YamlConfiguration setting){
		CONFIG = config;
		SETTING = setting;
		ID = setting.getString("ID");
		ITEM = createItem(m);

		setDataTag(plugin, "item", ID);
	}

	public ConfigAbstract getConfig() { return CONFIG; }
	public ItemStack getItem() {
		return ITEM;
	}
	public String getID(){ return ID; }
	public YamlConfiguration getSetting(){
		return SETTING;
	}

	public void setDataTag(JavaPlugin plugin, String k, String v){
		ItemUtils.setDataTag(plugin, ITEM, k, v);
	}

	public String getDataTag(JavaPlugin plugin, String k){
		return ItemUtils.getDataTag(plugin, ITEM, k);
	}

	public ItemStack createItem(Material m){
		ItemStack item = new ItemStack(m);
		item.setItemMeta(CONFIG.getLang().translateItem(this, item));

		return item;
	}
}