package io.github.mg138.tsbook.utils.config;

import io.github.mg138.tsbook.command.Armor;
import io.github.mg138.tsbook.utils.Translate;

import io.github.mg138.tsbook.utils.config.gui.armor.ArmorGUIConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class Config extends AbstractConfig {
    private static Config inst = null;
    ItemConfig itemConfig;
    Map<String, ConfigurationSection> mmMobs;
    ArmorGUIConfig armorGUIConfig = ArmorGUIConfig.getInstance();

    private Config(){
    }

    public static Config getInstance() {
        if (inst == null) inst = new Config();
        return inst;
    }

    public ArmorGUIConfig getArmorConfig() {
        return armorGUIConfig;
    }

    public ItemConfig getItemConfig() { return itemConfig; }

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
        itemConfig = new ItemConfig(cb.createMap("Items", "ID"), cb.createMap("Unidentified", "ID"));

        p.getLogger().info("Loading MythicMobs settings...");
        mmMobs = cb.createSectionMap("MythicMobs");

        p.getLogger().info("Loading GUI settings...");
        armorGUIConfig.load(cb.create("GUI/", "Equipment.yml"), translate);

        p.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }

    @Override
    public void unload() {
        itemConfig.items.clear();
        mmMobs.clear();
    }
}