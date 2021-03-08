package io.github.mg138.tsbook.utils;

import io.github.mg138.tsbook.util.RGBUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class Translate {
    private final YamlConfiguration language;

    public Translate(YamlConfiguration lang) {
        language = lang;
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
        return RGBUtil.INSTANCE.translate(string);
    }

    public String translate(String path) {
        return translate(path, null, language);
    }

    public String translate(String path, OfflinePlayer player) {
        return translate(path, player, language);
    }

    public String translate(String path, OfflinePlayer player, YamlConfiguration file) {
        return translateString(file.getString(path), player);
    }

    public String translate(String path, OfflinePlayer player, ConfigurationSection section) {
        return translateString(section.getString(path), player);
    }

    public List<String> translateList(String path) {
        return translateList(path, null, language);
    }

    public List<String> translateList(String path, OfflinePlayer player) {
        return translateList(path, player, language);
    }

    public List<String> translateList(String path, OfflinePlayer player, YamlConfiguration file) {
        return translateList(path, player, Objects.requireNonNull(file.getConfigurationSection("")));
    }

    public List<String> translateList(String path, OfflinePlayer player, ConfigurationSection section) {
        List<String> list = section.getStringList(path);

        if (list.isEmpty()) {
            try {
                String string = section.getString(path);
                if (string == null) return null;
                return translateByList(Arrays.asList(string.split("\\\\n|\\n")), player);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
        return translateByList(list, null);
    }

    public List<String> translateByList(List<String> list) {
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