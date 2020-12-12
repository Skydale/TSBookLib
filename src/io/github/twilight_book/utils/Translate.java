package io.github.twilight_book.utils;

import io.github.twilight_book.items.ItemAbstract;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Translate {
    YamlConfiguration LANG;

    public Translate(YamlConfiguration lang){
        LANG = lang;
    }

    public String translate(String s){
        return translate(s, null, LANG);
    }

    public String translate(String s, OfflinePlayer p){
        return translate(s, p, LANG);
    }

    public String translate(String s, OfflinePlayer p, YamlConfiguration lang){
        return transByString(lang.getString(s), p);
    }

    public String transByString(String s) {
        return transByString(s, null);
    }

    public String transByString(String s, OfflinePlayer p) {
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    /* ~ List<String> ~ */

    public List<String> translateList(String s){
        return translateList(s, null, LANG);
    }

    public List<String> translateList(String s, OfflinePlayer p){
        return translateList(s, p, LANG);
    }

    public List<String> translateList(String s, OfflinePlayer p, YamlConfiguration lang) {
        return transListByString(lang.getStringList(s), p);
    }

    public List<String> transListByString(String s) {
        return transListByString(s, null);
    }

    public List<String> transListByString(String s, OfflinePlayer p) {
        return Arrays.asList(PlaceholderAPI.setPlaceholders(p, s).split("\\n|\\\\n"));
    }

    public List<String> transListByString(List<String> s) {
        return transListByString(s, null);
    }

    public List<String> transListByString(List<String> s, OfflinePlayer p) {
        List<String> r = new ArrayList<>();
        try {
            for (String value : s) {
                r.add(PlaceholderAPI.setPlaceholders(p, value));
            }
        } catch (NullPointerException e) {
            return Collections.emptyList();
        }
        return r;
    }

    public String replaceItem(String path, String ID, YamlConfiguration file){
        String s = file.getString(path);

        if (s == null) {
            return null;
        }

        return s.replaceAll("item_", "item_" + ID + ".");
    }

    public ItemMeta translateItem(ItemAbstract itemAbstract, ItemStack item){
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(transByString( transByString(replaceItem("FORMAT.NAME", itemAbstract.getID(), itemAbstract.getSetting()))));
        meta.setLore(transListByString(transListByString(replaceItem("FORMAT.LORE", itemAbstract.getID(), itemAbstract.getSetting()))));
        return meta;
    }
}
