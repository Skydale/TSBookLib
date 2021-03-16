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
    fun create(path: String, target: String): YamlConfiguration {
        val file = File(plugin.dataFolder, path + target)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource(path + target, false)
        }
        val yaml = YamlConfiguration()
        try {
            yaml.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
        return yaml
    }

    fun createFolder(path: String, target: String): YamlConfiguration {
        val files = loadJarContent(path)
        val yaml = YamlConfiguration()
        files!!.forEach(Consumer { value: File ->
            if (value.name == target) {
                try {
                    yaml.load(value)
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: InvalidConfigurationException) {
                    e.printStackTrace()
                }
            }
        })
        return yaml
    }

    fun createFolder(path: String): List<YamlConfiguration> {
        val files = loadJarContent(path)
        val yamlList: MutableList<YamlConfiguration> = ArrayList()
        for (t in files!!) {
            yamlList.add(YamlConfiguration.loadConfiguration(t))
        }
        return yamlList
    }

    fun createMap(path: String, key: String): Map<String, YamlConfiguration>? {
        val files = loadJarContent(path)
        val yamlMap: MutableMap<String, YamlConfiguration> = HashMap()
        try {
            for (file in files!!) {
                val yaml = YamlConfiguration()
                yaml.load(file)
                yamlMap[yaml.getString(key)!!] = yaml
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
            return null
        }
        return yamlMap
    }

    fun createSectionMap(path: String): Map<String, ConfigurationSection>? {
        val files = loadJarContent(path)
        val yamlMap: MutableMap<String, ConfigurationSection> = HashMap()
        try {
            for (file in files!!) {
                val yaml = YamlConfiguration()
                yaml.load(file)
                for (section in yaml.getKeys(false)) {
                    yamlMap[section] = yaml.getConfigurationSection(section)!!
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
            return null
        }
        return yamlMap
    }

    private fun loadJarContent(directory: String): List<File>? {
        val folder = File(plugin.dataFolder, directory)
        if (!folder.exists()) {
            folder.mkdirs()
            val jarFile = try {
                JarFile(jar)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val name = entry.name
                if (!name.startsWith("$directory/") || entry.isDirectory) continue
                if (!File(plugin.dataFolder, name).exists()) plugin.saveResource(name, false)
            }
        }
        return recursiveListFolder(ArrayList(), folder)
    }

    private fun recursiveListFolder(list: MutableList<File>, folder: File): List<File> {
        for (file in folder.listFiles()!!) {
            if (file.isDirectory) {
                recursiveListFolder(list, file)
            } else {
                list.add(file)
            }
        }
        return list
    }
}