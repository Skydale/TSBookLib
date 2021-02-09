package io.github.mg138.tsbook.utils.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public abstract class AbstractItemConfig {
    final HashMap<String, YamlConfiguration> items;
    final HashMap<String, YamlConfiguration> unid;

    public AbstractItemConfig(HashMap<String, YamlConfiguration> items, HashMap<String, YamlConfiguration> unid) {
        this.items = items;
        this.unid = unid;
    }
}
