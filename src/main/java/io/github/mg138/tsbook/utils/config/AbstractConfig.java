package io.github.mg138.tsbook.utils.config;

import io.github.mg138.tsbook.utils.Translate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class AbstractConfig {
    public YamlConfiguration config;
    public JavaPlugin plugin;
    public File JAR;
    public Translate translate;

    public abstract void load(JavaPlugin p, File j);

    public abstract void unload();
}
