package io.github.mg138.tsbook.setting;

import io.github.mg138.tsbook.setting.config.BookConfig;
import io.github.mg138.tsbook.utils.Translate;
import io.github.mg138.tsbook.setting.gui.ArmorGUIConfig;
import io.github.mg138.tsbook.setting.item.ItemConfig;
import io.github.mg138.tsbook.setting.util.ConfigBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class BookSetting extends AbstractSetting {
    private static final BookSetting inst = new BookSetting();

    private BookSetting(){
    }

    public static BookSetting getInstance() {
        return inst;
    }

    public final ArmorGUIConfig armorGUIConfig = ArmorGUIConfig.getInstance();
    public final ItemConfig itemConfig = ItemConfig.getInstance();

    private Map<String, ConfigurationSection> mmMobs;
    public ConfigurationSection getMMMob(String ID) {
        return mmMobs.get(ID);
    }

    public BookConfig bookConfig;

    @Override
    public void load(JavaPlugin plugin, File jar) {
        this.plugin = plugin;
        this.jar = jar;

        long start = System.currentTimeMillis();

        ConfigBuilder cb = new ConfigBuilder(plugin, jar);

        plugin.getLogger().info("Loading configuration...");
        bookConfig = new BookConfig(cb.create("", "config.yml"));

        plugin.getLogger().info("Loading language file: " + bookConfig.locale + "...");
        translate = new Translate(cb.createFolder("lang", bookConfig.locale + ".yml"));

        plugin.getLogger().info("Loading item settings...");
        itemConfig.load(cb.createMap("Items", "ID"), cb.createMap("Unidentified", "ID"));

        plugin.getLogger().info("Loading MythicMobs settings...");
        mmMobs = cb.createSectionMap("MythicMobs");

        plugin.getLogger().info("Loading GUI settings...");
        armorGUIConfig.load(cb.create("GUI/", "Equipment.yml"), translate);

        plugin.getLogger().info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!");
    }

    @Override
    public void unload() {
        itemConfig.unload();
        mmMobs.clear();
    }
}