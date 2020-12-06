package io.github.twilight_book.Utils.Config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config extends ConfigAbstract {

    @Override
    public void setup(JavaPlugin p, File j){
        plugin = p;
        JAR = j;

        long start = System.currentTimeMillis();

        ConfigBuilder cb = new ConfigBuilder(p, j);

        p.getLogger().info("Loading configuration...");
        config = cb.create("", "config.yml");

        p.getLogger().info("Loading language file: " + config.getString("locale") + "...");
        langFile = cb.createFolder("lang", config.getString("locale") + ".yml");

        p.getLogger().info("Loading item settings...");
        items = cb.createFolder("items");

        p.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }
}