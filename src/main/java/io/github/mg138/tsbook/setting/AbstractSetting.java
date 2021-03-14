package io.github.mg138.tsbook.setting;

import io.github.mg138.tsbook.utils.Translate;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class AbstractSetting {
    public JavaPlugin plugin;
    public File jar;
    public Translate translate;

    public abstract void load(JavaPlugin plugin, File jar);

    public abstract void unload();
}
