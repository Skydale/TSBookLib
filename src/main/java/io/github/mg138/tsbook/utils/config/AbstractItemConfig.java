package io.github.mg138.tsbook.utils.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;

public abstract class AbstractItemConfig {
    final Map<String, YamlConfiguration> items;
    final Map<String, YamlConfiguration> unid;

    public AbstractItemConfig(Map<String, YamlConfiguration> items, Map<String, YamlConfiguration> unid) {
        this.items = items;
        this.unid = unid;
    }
}
