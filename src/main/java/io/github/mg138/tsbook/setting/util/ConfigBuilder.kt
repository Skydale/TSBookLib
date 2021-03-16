package io.github.mg138.tsbook.setting.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigBuilder {
    final JavaPlugin plugin;
    final File jar;

    public ConfigBuilder(JavaPlugin plugin, File jar) {
        this.plugin = plugin;
        this.jar = jar;
    }

    public YamlConfiguration create(String path, String target) {
        File file = new File(plugin.getDataFolder(), path + target);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(path + target, false);
        }

        YamlConfiguration yaml = new YamlConfiguration();

        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yaml;
    }

    public YamlConfiguration createFolder(String path, String target) {
        List<File> files = loadJarContent(path);

        YamlConfiguration yaml = new YamlConfiguration();

        files.forEach((value) -> {
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

    public List<YamlConfiguration> createFolder(String path) {
        List<File> files = loadJarContent(path);

        List<YamlConfiguration> YAMLs = new ArrayList<>();

        for (File t : files) {
            YAMLs.add(YamlConfiguration.loadConfiguration(t));
        }

        return YAMLs;
    }

    public Map<String, YamlConfiguration> createMap(String path, String key) {
        List<File> files = loadJarContent(path);

        Map<String, YamlConfiguration> MappedYAML = new HashMap<>();
        try {
            for (File file : files) {
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(file);
                MappedYAML.put(yaml.getString(key), yaml);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        return MappedYAML;
    }

    public Map<String, ConfigurationSection> createSectionMap(String path) {
        List<File> files = loadJarContent(path);

        Map<String, ConfigurationSection> MappedYAML = new HashMap<>();
        try {
            for (File file : files) {
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(file);
                for (String section : yaml.getKeys(false)) {
                    MappedYAML.put(section, yaml.getConfigurationSection(section));
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        return MappedYAML;
    }

    List<File> loadJarContent(String directory) {
        File folder = new File(plugin.getDataFolder(), directory);

        if (!folder.exists()) {
            folder.mkdirs();

            JarFile jarFile;
            try {
                jarFile = new JarFile(jar);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!(name.startsWith(directory + "/")) || entry.isDirectory()) continue;

                if (!(new File(plugin.getDataFolder(), name).exists())) plugin.saveResource(name, false);
            }
        }

        return listFolderContent(new ArrayList<>(), folder);
    }

    List<File> listFolderContent(List<File> list, File folder) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                listFolderContent(list, file);
            } else {
                list.add(file);
            }
        }
        return list;
    }
}