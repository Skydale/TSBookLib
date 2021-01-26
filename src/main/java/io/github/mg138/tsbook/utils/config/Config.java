package io.github.mg138.tsbook.utils.config;

import io.github.mg138.tsbook.utils.Translate;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config extends AbstractConfig {

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
        items = cb.createFileMap("Items");
        unid = cb.createFileMap("Unidentified");

        p.getLogger().info("Loading MythicMobs settings...");
        mmMobs = cb.createSectionMap("MythicMobs");

        p.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }

    @Override
    public void unload() {
        items.clear();
        mmMobs.clear();
    }
}