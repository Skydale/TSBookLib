package io.github.twilight_book.utils;

import io.github.twilight_book.items.ItemAbstract;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Translate {
    YamlConfiguration langFile;

    public Translate(YamlConfiguration lf){
        langFile = lf;
    }
    public String translate(String s){
        return translate(s, null);
    }

    public String translate(String s, OfflinePlayer p) {
        String t = langFile.getString(s);

        if (t != null) {
            return PlaceholderAPI.setPlaceholders(p, t);
        }
        return "";
    }

    public List<String> translateList(String s){
        return translateList(s, null);
    }

    public List<String> translateList(String s, OfflinePlayer p) {
        List<String> t = langFile.getStringList(s);

        if (t.isEmpty()){
            String get = langFile.getString(s);

            if(get != null) {
                return Arrays.asList(PlaceholderAPI.setPlaceholders(p, get).split("\\n|\\\\n"));
            }
        } else {
            List<String> r = new ArrayList<>();
            try {
                for (String value : t) {
                    r.add(PlaceholderAPI.setPlaceholders(p, value));
                }
            } catch (NullPointerException e) {
                return Collections.emptyList();
            }
            return r;
        }
        return Collections.emptyList();
    }

    public ItemMeta translateItem(ItemAbstract itemAbstract){
        ItemMeta meta = itemAbstract.getMeta();
        meta.setDisplayName(translate("items.format.name"));
        return meta;
    }
}
