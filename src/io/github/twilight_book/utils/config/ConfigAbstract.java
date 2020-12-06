package io.github.twilight_book.utils.config;

import io.github.twilight_book.utils.Translate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public abstract class ConfigAbstract {
    protected JavaPlugin plugin;
    protected File JAR;
    protected Translate lang;
    protected YamlConfiguration config;
    protected YamlConfiguration langFile;
    protected List<YamlConfiguration> items;

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

    public abstract void setup(JavaPlugin p, File j);
}
