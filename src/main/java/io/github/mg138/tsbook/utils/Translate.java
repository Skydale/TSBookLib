package io.github.mg138.tsbook.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import tsp.hexchat.util.RGBUtil;

import java.util.*;

public class Translate {
    final YamlConfiguration LANG;

    public Translate(YamlConfiguration lang) {
        LANG = lang;
    }

    public String translateString(String string) {
        return translateString(string, null);
    }

    public String translateString(String string, OfflinePlayer player) {
        while (PlaceholderAPI.containsPlaceholders(string)) {
            String newString = PlaceholderAPI.setPlaceholders(player, string);
            if (string.equals(newString)) throw new IllegalArgumentException("Placeholder stuck in a loop.");
            string = newString;
        }
        return RGBUtil.format(string);
    }

    public String translate(String path) {
        return translate(path, null, LANG);
    }

    public String translate(String path, OfflinePlayer player) {
        return translate(path, player, LANG);
    }

    public String translate(String path, OfflinePlayer player, YamlConfiguration file) {
        return translateString(file.getString(path), player);
    }

    public List<String> translateList(String path) {
        return translateList(path, null, LANG);
    }

    public List<String> translateList(String path, OfflinePlayer player) {
        return translateList(path, player, LANG);
    }

    public List<String> translateList(String path, OfflinePlayer player, YamlConfiguration file) {
        List<String> list = file.getStringList(path);

        if (list.isEmpty()) {
            try {
                String string = file.getString(path);
                if (string == null) return null;
                return translateByList(Arrays.asList(string.split("\\\\n|\\n")), player);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
        return translateByList(list, null);
    }

    public List<String> translateByList(List<String> list, OfflinePlayer player) {
        List<String> result = new ArrayList<>();
        try {
            for (String string : list) {
                result.addAll(Arrays.asList(translateString(string, player).split("\\\\n|\\n")));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return result;
    }
}