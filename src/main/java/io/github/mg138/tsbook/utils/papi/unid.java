package io.github.mg138.tsbook.utils.papi;

import io.github.mg138.tsbook.utils.config.Config;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class unid extends PlaceholderExpansion {
    final JavaPlugin plugin;
    final Config config;

    public unid(JavaPlugin p, Config c) {
        plugin = p;
        config = c;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "unid";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        String item = identifier.replaceAll("\\..*", "");
        String path = identifier.replaceAll(".*?\\.", "").replaceAll("-", ".");
        YamlConfiguration itemSetting = config.getUnidentifiedByID(item);

        if (!itemSetting.contains(path)) return null;

        List<String> list = itemSetting.getStringList(path);
        if (list.isEmpty()) return itemSetting.getString(path);
        return String.join("\n", list);
    }
}