package io.github.twilight_book.utils.config;

import io.github.twilight_book.utils.lang.Translate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.Set;

public abstract class ConfigAbstract {
    JavaPlugin plugin;
    File JAR;
    Translate lang;
    YamlConfiguration config;
    YamlConfiguration langFile;
    Map<String, YamlConfiguration> items;
    Map<String, YamlConfiguration> mmMobs;
    Map<String, YamlConfiguration> unidentified;

    public JavaPlugin getPlugin(){
        return plugin;
    }

    public File getJAR(){
        return JAR;
    }

    public YamlConfiguration getConfig(){
        return config;
    }

    public YamlConfiguration getLangFile(){
        return langFile;
    }

    public Translate getLang(){
        return lang;
    }

    public Set<String> getItems() { return items.keySet(); }
    public Set<String> getUnidentified() { return unidentified.keySet(); }
    public YamlConfiguration getItemByID(String ID) { return items.get(ID); }
    public YamlConfiguration getUnidentifiedByID(String ID) { return unidentified.get(ID); }
    public ConfigurationSection getMMMob(String ID) { return mmMobs.get(ID); }

    public abstract void setup(JavaPlugin p, File j);
    public abstract void unload();
}
