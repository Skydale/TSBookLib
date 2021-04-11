package io.github.mg138.tsbook.util.translate

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection

class TranslatableSetting(private val setting: ConfigurationSection, val placeholders: MutableMap<String, String> = HashMap()) {
    fun getSection(path: String): TranslatableSetting {
        try {
            return TranslatableSetting(setting.getConfigurationSection(path)!!, placeholders)
        } catch (e: KotlinNullPointerException) {
            throw IllegalArgumentException("${setting.name} doesn't contain $path")
        }
    }

    fun getUnsafe(path: String, player: OfflinePlayer? = null): String? {
        return TranslateUtil.getUnsafe(path, player, setting, placeholders)
    }

    fun get(path: String, player: OfflinePlayer? = null): String {
        return TranslateUtil.get(path, player, setting, placeholders)
    }

    fun getList(path: String, player: OfflinePlayer? = null): List<String> {
        return TranslateUtil.getList(path, player, setting, placeholders)
    }

    override fun toString(): String {
        return setting.getKeys(false).toString()
    }
}