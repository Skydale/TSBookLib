package io.github.twilight_book.utils.lang;

import io.github.twilight_book.items.ItemTemplate;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Translate {
    final YamlConfiguration LANG;

    public Translate(YamlConfiguration lang) {
        LANG = lang;
    }

    public String translateString(String s) {
        return translateString(s, null);
    }

    public String translateString(String s, OfflinePlayer p) {
        while (PlaceholderAPI.containsPlaceholders(s)) {
            if (s.equals(PlaceholderAPI.setPlaceholders(p, s))) throw new IllegalArgumentException("Placeholder stuck in a loop.");
            s = PlaceholderAPI.setPlaceholders(p, s);
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String translate(String path) {
        return translate(path, null, LANG);
    }

    public String translate(String path, OfflinePlayer p) {
        return translate(path, p, LANG);
    }

    public String translate(String path, OfflinePlayer p, YamlConfiguration file) {
        return translateString(file.getString(path), p);
    }

    /*
    #                                                  #
    #                  List<String>                    #
    #                                                  #
    */

    public List<String> translateList(String path) {
        return translateList(path, null, LANG);
    }

    public List<String> translateList(String path, OfflinePlayer p) {
        return translateList(path, p, LANG);
    }

    public List<String> translateList(String path, OfflinePlayer p, YamlConfiguration file) {
        List<String> l = file.getStringList(path);

        if (l.isEmpty()) {
            try {
                return translateByList(Arrays.asList(file.getString(path).split("\\\\n|\\n")), p);
            } catch (NullPointerException e){
                e.printStackTrace();
                return null;
            }
        }
        return translateByList(l, null);
    }

    public List<String> translateByList(List<String> l, OfflinePlayer p) {
        List<String> r = new ArrayList<>();
        try {
            for (String s : l) {
                r.addAll(Arrays.asList(translateString(s, p).split("\\\\n|\\n")));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return r;
    }

    public ItemMeta translateItem(ItemTemplate itemTemplate, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(translate("FORMAT.NAME", null, itemTemplate.getSetting()));
        meta.setLore(   translateList("FORMAT.LORE", null, itemTemplate.getSetting()));
        return meta;
    }
}