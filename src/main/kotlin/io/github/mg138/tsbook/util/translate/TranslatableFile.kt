package io.github.mg138.tsbook.util.translate

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

class TranslatableFile(private val yamlFile: YamlConfiguration) {
    val placeholders: MutableMap<String, String> = HashMap()

    fun getUnsafe(path: String, player: OfflinePlayer? = null): String? {
        return TranslateUtil.getUnsafe(path, player, yamlFile, placeholders)
    }

    fun get(path: String, player: OfflinePlayer? = null): String {
        return TranslateUtil.get(path, player, yamlFile, placeholders)
    }

    fun getList(path: String, player: OfflinePlayer? = null): List<String> {
        return TranslateUtil.getList(path, player, yamlFile, placeholders)
    }
}