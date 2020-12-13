package io.github.twilight_book.utils.config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigBuilder{
    final JavaPlugin plugin;
    final File JAR;

    public ConfigBuilder(JavaPlugin p, File jar){
        plugin = p;
        JAR = jar;
    }

    File createFile(String path, String target){
        File file = new File(plugin.getDataFolder() + File.separator + path, target);
        if (!(file.exists())) {
            if(!(file.getParentFile().mkdirs())){
                plugin.getLogger().severe("Something failed to load!");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return null;
            }
            plugin.saveResource(path + target, false);
        }
        return file;
    }

    public YamlConfiguration create(String path, String target) {
        YamlConfiguration YAML = new YamlConfiguration();
        try {
            YAML.load(createFile(path, target));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
		return YAML;
    }

    public Map<String, ConfigurationSection> createMap(String path, String target) {
        Map<String, ConfigurationSection> YAML = new HashMap<>();

        try {
            YamlConfiguration temp = new YamlConfiguration();
            temp.load(createFile(path, target));
            temp.getKeys(false).forEach(value -> YAML.put(value, temp.getConfigurationSection(value)));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        return YAML;
    }

    public YamlConfiguration createFolder(String directory, String target){
        List<File> list = loadJarContent(directory);

        for (File entry : list){
            if(entry.getName().equals(target)){
                YamlConfiguration YAML = new YamlConfiguration();
                try {
                    YAML.load(entry);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
                return YAML;
            }
        }
        return null;
    }

    public Map<String, YamlConfiguration> createFolder(String directory){
        List<File> list = loadJarContent(directory);

        Map<String, YamlConfiguration> listYAML = new HashMap<>();
        list.forEach(value -> {
            YamlConfiguration temp = new YamlConfiguration();
            try {
                temp.load(value);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            listYAML.put(temp.getString("ID"), temp);
        });
        return listYAML;
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
}