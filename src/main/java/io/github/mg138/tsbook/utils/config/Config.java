package io.github.mg138.tsbook.utils.config;

import io.github.mg138.tsbook.utils.Translate;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class Config extends AbstractConfig {
    YamlConfiguration langFile;
    ItemConfig itemConfig;
    HashMap<String, ConfigurationSection> mmMobs;

    public ItemConfig getItemConfig() { return itemConfig; }

    public YamlConfiguration getConfig() {
        return config;
    }

    public ConfigurationSection getMMMob(String ID) {
        return mmMobs.get(ID);
    }

    @Override
    public void setup(JavaPlugin p, File j) {
        plugin = p;
        JAR = j;

        long start = System.currentTimeMillis();

        ConfigBuilder cb = new ConfigBuilder(p, j);

        p.getLogger().info("Loading configuration...");
        config = cb.create("", "config.yml");

        p.getLogger().info("Loading language file: " + config.getString("locale") + "...");
        langFile = cb.createFolder("lang", config.getString("locale") + ".yml");
        translate = new Translate(langFile);

        p.getLogger().info("Loading item settings...");
        itemConfig = new ItemConfig(cb.createFileMap("Items"), cb.createFileMap("Unidentified"));

        p.getLogger().info("Loading MythicMobs settings...");
        mmMobs = cb.createSectionMap("MythicMobs");

        p.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }

    @Override
    public void unload() {
        itemConfig.items.clear();
        mmMobs.clear();
    }
}