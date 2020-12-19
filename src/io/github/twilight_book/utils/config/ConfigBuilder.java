package io.github.twilight_book.utils.config;
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

    public YamlConfiguration create(String path, String target){
        File file = new File(plugin.getDataFolder(), target);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(target, false);
        }

        YamlConfiguration yaml = new YamlConfiguration();

        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yaml;
    }

    public YamlConfiguration createFolder(String path, String target){
        List<File> listFile = loadJarContent(path);

        YamlConfiguration yaml = new YamlConfiguration();

        listFile.forEach((value) -> {
            if (value.getName().equals(target)) {
                try {
                    yaml.load(value);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });

        return yaml;
    }

    public List<YamlConfiguration> createFolder(String path){
        List<File> listFile = loadJarContent(path);

        List<YamlConfiguration> yamlList = new ArrayList<>();

        for (File t : listFile){
            yamlList.add(YamlConfiguration.loadConfiguration(t));
        }

        return yamlList;
    }

    public Map<String, YamlConfiguration> createMap(String path){
        List<File> listFile = loadJarContent(path);

        Map<String, YamlConfiguration> yamlMap = new HashMap<>();
        try {
            for (File t : listFile){
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(t);
                yamlMap.put(yaml.getString("ID"), yaml);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace(); return null;
        }

        return yamlMap;
    }

    List<File> loadJarContent(String directory){
        File folder = new File(plugin.getDataFolder(), directory);

        if (!folder.exists()) {
            folder.mkdirs();

            JarFile jarFile;
            try {
                jarFile = new JarFile(JAR);
            } catch (IOException e) {
                e.printStackTrace(); return null;
            }
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!(name.startsWith(directory + "/")) || entry.isDirectory()) continue;

                if(!(new File(plugin.getDataFolder(), name).exists())) plugin.saveResource(name, false);
            }
        }

        return listFolderContent(new ArrayList<>(), folder);
    }

    List<File> listFolderContent(List<File> list, File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                listFolderContent(list, file);
            } else {
                list.add(file);
            }
        }
        return list;
    }
}