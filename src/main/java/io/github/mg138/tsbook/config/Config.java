package io.github.mg138.tsbook.config;

import io.github.mg138.tsbook.utils.Translate;
import io.github.mg138.tsbook.config.gui.ArmorGUIConfig;
import io.github.mg138.tsbook.config.item.ItemConfig;
import io.github.mg138.tsbook.config.util.ConfigBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class Config extends AbstractConfig {
    private static final Config inst = new Config();

    private Config(){
    }

    public static Config getInstance() {
        return inst;
    }

    Map<String, ConfigurationSection> mmMobs;
    public final ArmorGUIConfig armorGUIConfig = ArmorGUIConfig.getInstance();
    public final ItemConfig itemConfig = ItemConfig.getInstance();

    public YamlConfiguration getConfig() {
        return config;
    }

    public ConfigurationSection getMMMob(String ID) {
        return mmMobs.get(ID);
    }

    @Override
    public void load(JavaPlugin p, File j) {
        plugin = p;
        JAR = j;

        long start = System.currentTimeMillis();

        ConfigBuilder cb = new ConfigBuilder(p, j);

        p.getLogger().info("Loading configuration...");
        config = cb.create("", "config.yml");

        p.getLogger().info("Loading language file: " + config.getString("locale") + "...");
        YamlConfiguration langFile = cb.createFolder("lang", config.getString("locale") + ".yml");
        translate = new Translate(langFile);

        p.getLogger().info("Loading item settings...");
        itemConfig.load(cb.createMap("Items", "ID"), cb.createMap("Unidentified", "ID"));

        p.getLogger().info("Loading MythicMobs settings...");
        mmMobs = cb.createSectionMap("MythicMobs");

        p.getLogger().info("Loading GUI settings...");
        armorGUIConfig.load(cb.create("GUI/", "Equipment.yml"), translate);

        p.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }

    @Override
    public void unload() {
        itemConfig.unload();
        mmMobs.clear();
    }
}