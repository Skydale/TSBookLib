package io.github.mg138.tsbook.utils.config;

import io.github.mg138.tsbook.utils.Translate;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.Set;

public abstract class AbstractConfig {
    JavaPlugin plugin;
    File JAR;
    Translate translate;
    YamlConfiguration config;
    YamlConfiguration langFile;
    Map<String, YamlConfiguration> items;
    Map<String, YamlConfiguration> unid;
    Map<String, ConfigurationSection> mmMobs;

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public File getJAR() {
        return JAR;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public YamlConfiguration getLangFile() {
        return langFile;
    }

    public Translate getTranslate() {
        return translate;
    }

    public Set<String> getItems() {
        return items.keySet();
    }

    public Set<String> getUnidentified() {
        return unid.keySet();
    }

    public YamlConfiguration getItemByID(String ID) {
        return items.get(ID);
    }

    public YamlConfiguration getUnidentifiedByID(String ID) {
        return unid.get(ID);
    }

    public YamlConfiguration getAnyItemByID(String ID) {
        if (items.containsKey(ID)) return getItemByID(ID);
        if (unid.containsKey(ID)) return getUnidentifiedByID(ID);
        return null;
    }

    public ConfigurationSection getMMMob(String ID) {
        return mmMobs.get(ID);
    }

    public abstract void setup(JavaPlugin p, File j);

    public abstract void unload();
}
