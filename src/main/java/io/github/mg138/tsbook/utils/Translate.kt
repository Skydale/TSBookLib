package io.github.mg138.tsbook.utils

import io.github.mg138.tsbook.util.RGBUtil
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.util.*

class Translate(private val language: YamlConfiguration) {
    fun translateString(string: String, player: OfflinePlayer? = null): String {
        var copy = string
        var newString: String
        while (PlaceholderAPI.containsPlaceholders(copy)) {
            newString = PlaceholderAPI.setPlaceholders(player, copy)
            require(copy != newString) {
                """Placeholder stuck in a loop. (Does it contain placeholders that don't exist?)
		           The error string: $newString"""
            }
            copy = newString
        }
        return RGBUtil.translate(copy)
    }

    fun translate(path: String, player: OfflinePlayer? = null, file: YamlConfiguration = language): String {
        return translateString(file.getString(path)!!, player)
    }

    fun translate(path: String, player: OfflinePlayer?, section: ConfigurationSection): String {
        return translateString(section.getString(path)!!, player)
    }

    fun translateList(path: String, player: OfflinePlayer? = null, file: YamlConfiguration = language): List<String> {
        return translateList(path, player, file.getConfigurationSection("")!!)
    }

    fun translateList(path: String, player: OfflinePlayer?, section: ConfigurationSection): List<String> {
        val list = section.getStringList(path)
        return when {
            list.isEmpty() -> {
                try {
                    val string = section.getString(path) ?: return emptyList()
                    translateByList(string.split("\\\\n|\\n"), player)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    return emptyList()
                }
            }
            else -> translateByList(list, player)
        }
    }

    fun translateByList(list: List<String>, player: OfflinePlayer? = null): List<String> {
        val result: MutableList<String> = ArrayList()
        try {
            list.forEach { string ->
                result.addAll(translateString(string, player).split("\\\\n|\\n"))
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return emptyList()
        }
        return result
    }
}