package io.github.mg138.tsbook.setting.util

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarFile

class ConfigBuilder(private val plugin: JavaPlugin, jar: File) {
    private val dataFolder = plugin.dataFolder
    private val jarFile = JarFile(jar)

    fun create(path: String, target: String): YamlConfiguration {
        val to = path + target
        val file = File(dataFolder, to)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource(to, false)
        }
        return YamlConfiguration().also {
            it.load(file)
        }
    }

    fun loadDirectory(path: String, target: String): YamlConfiguration {
        val yaml = YamlConfiguration()
        loadDirectoryContent(path).forEach { file ->
            if (file.name == target) {
                yaml.load(file)
            }
        }
        return yaml
    }

    fun loadDirectory(path: String): List<YamlConfiguration> {
        val list: MutableList<YamlConfiguration> = ArrayList()
        loadDirectoryContent(path).forEach { file ->
            list.add(YamlConfiguration.loadConfiguration(file))
        }
        return list
    }

    fun loadToMap(path: String, key: String): Map<String, YamlConfiguration> {
        val map: MutableMap<String, YamlConfiguration> = HashMap()
        loadDirectory(path).forEach { yaml ->
            map[yaml.getString(key)!!] = yaml
        }
        return map
    }

    fun loadToSectionMap(path: String): Map<String, ConfigurationSection> {
        val map: MutableMap<String, ConfigurationSection> = HashMap()
        loadDirectory(path).forEach { yaml ->
            yaml.getKeys(false).forEach { key ->
                map[key] = yaml.getConfigurationSection(key)!!
            }
        }
        return map
    }

    private fun loadDirectoryContent(directory: String): List<File> {
        val folder = File(dataFolder, directory)
        if (!folder.exists()) {
            folder.mkdirs()

            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.isDirectory) continue

                val name = entry.name
                if (!name.startsWith("$directory/")) continue

                if (!File(dataFolder, name).exists()) plugin.saveResource(name, false)
            }
        }
        val result: MutableList<File> = ArrayList()
        recursiveListDirectory(result, folder)
        return result
    }

    private fun recursiveListDirectory(list: MutableList<File>, folder: File) {
        folder.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                recursiveListDirectory(list, file)
            } else {
                list.add(file)
            }
        }
    }
}