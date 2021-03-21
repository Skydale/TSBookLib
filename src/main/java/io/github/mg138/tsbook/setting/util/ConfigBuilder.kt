package io.github.mg138.tsbook.setting.util

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.*
import java.util.jar.JarFile
import java.util.function.Consumer

class ConfigBuilder(private val plugin: JavaPlugin, private val jar: File) {
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

    fun createFolder(path: String, target: String): YamlConfiguration {
        val yaml = YamlConfiguration()
        loadJarContent(path).forEach { file ->
            if (file.name == target) {
                yaml.load(file)
            }
        }
        return yaml
    }

    fun createFolder(path: String): List<YamlConfiguration> {
        val list: MutableList<YamlConfiguration> = ArrayList()
        loadJarContent(path).forEach { file ->
            list.add(YamlConfiguration.loadConfiguration(file))
        }
        return list
    }

    fun createMap(path: String, key: String): Map<String, YamlConfiguration> {
        val map: MutableMap<String, YamlConfiguration> = HashMap()
        loadJarContent(path).forEach { file ->
            YamlConfiguration().apply {
                load(file)
                map[getString(key)!!] = this
            }
        }
        return map
    }

    fun createSectionMap(path: String): Map<String, ConfigurationSection> {
        val map: MutableMap<String, ConfigurationSection> = HashMap()
        loadJarContent(path).forEach { file ->
            YamlConfiguration().apply {
                load(file)
                getKeys(false).forEach { section ->
                    map[section] = getConfigurationSection(section)!!
                }
            }
        }
        return map
    }

    private fun loadJarContent(directory: String): List<File> {
        val folder = File( dataFolder, directory)
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
        recursiveListFolder(result, folder)
        return result
    }

    private fun recursiveListFolder(list: MutableList<File>, folder: File) {
        folder.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                recursiveListFolder(list, file)
            } else {
                list.add(file)
            }
        }
    }
}