package io.github.twilight_book.utils.config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigBuilder{
    JavaPlugin plugin;
    File JAR;

    public ConfigBuilder(JavaPlugin p, File jar){
        plugin = p;
        JAR = jar;
    }

    public YamlConfiguration create(String path, String name) {
        File file = new File(plugin.getDataFolder() + File.separator + path, name);
        if (!(file.exists())) {
            if(!(file.getParentFile().mkdirs())){
                plugin.getLogger().severe("Something failed to load!");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return null;
            }
            plugin.saveResource(name, false);
        }

        YamlConfiguration t = new YamlConfiguration();
        try {
            t.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
		return t;
    }

    List<File> loadJarContent(String directory){
        File folder = new File(plugin.getDataFolder() + File.separator + directory);
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();

        JarFile jarFile;
        try {
            jarFile = new JarFile(JAR);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }

        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (!(name.startsWith(directory + "/")) || entry.isDirectory()) {
                continue;
            }
            if(!((new File(plugin.getDataFolder() + File.separator + name)).exists())) {
                plugin.saveResource(name, false);
            }
        }

        List<File> list = new ArrayList<>();
        return listFolderContent(list, folder);
    }

    List<File> listFolderContent(List<File> list, File folder) {
        for (File entry : folder.listFiles()) {
            if (entry.isDirectory()) {
                listFolderContent(list, entry);
            } else {
                list.add(entry);
            }
        }
        return list;
    }

    public List<YamlConfiguration> createFolder(String directory){
        List<File> list = loadJarContent(directory);

        List<YamlConfiguration> listYAML = new ArrayList<>();
        list.forEach(value -> {
            try {
                YamlConfiguration temp = new YamlConfiguration();
                temp.load(value);
                listYAML.add(temp);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        });
        return listYAML;
    }

    public YamlConfiguration createFolder(String directory, String target){
        List<File> list = loadJarContent(directory);

        for (File entry : list){
            if(entry.getName() == target){
                YamlConfiguration config = new YamlConfiguration();
                try {
                    config.load(entry);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
                return config;
            }
        }
        return null;
    }
}
