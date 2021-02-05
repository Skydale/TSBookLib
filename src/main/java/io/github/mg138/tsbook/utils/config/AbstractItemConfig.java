package io.github.mg138.tsbook.utils.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractItemConfig {
    HashMap<String, YamlConfiguration> items;
    HashMap<String, YamlConfiguration> unid;

    public AbstractItemConfig(HashMap<String, YamlConfiguration> items, HashMap<String, YamlConfiguration> unid) {
        this.items = items;
        this.unid = unid;
    }
}
