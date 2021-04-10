package io.github.mg138.tsbook.util

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

class LanguageFile(private val languageFile: YamlConfiguration) {
    fun getUnsafe(path: String, player: OfflinePlayer? = null): String? {
        return Translate.getUnsafe(path, player, languageFile)
    }

    fun get(path: String, player: OfflinePlayer? = null): String {
        return Translate.get(path, player, languageFile)
    }

    fun getList(path: String, player: OfflinePlayer? = null): List<String> {
        return Translate.getList(path, player, languageFile)
    }
}