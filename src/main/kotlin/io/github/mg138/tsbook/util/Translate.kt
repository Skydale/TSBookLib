package io.github.mg138.tsbook.util

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.lang.NullPointerException
import java.util.*

class Translate(private val language: YamlConfiguration) {
    fun translate(path: String, player: OfflinePlayer? = null, section: ConfigurationSection = language): String {
        section.getString(path)?.let {
            return translateString(it, player)
        } ?: throw NullPointerException("Invalid path \"$path\"!")
    }

    fun translateString(string: String, player: OfflinePlayer? = null): String {
        var copy = string
        copy = PlaceholderAPI.setPlaceholders(player, copy)
        /*
        var newString: String
        while (PlaceholderAPI.containsPlaceholders(copy)) {
            newString = PlaceholderAPI.setPlaceholders(player, copy)
            require(copy != newString) {
                """Placeholder stuck in a loop. (Does it contain placeholders that don't exist?)
		           The error string: $newString"""
            }
            copy = newString
        }
         */
        return RGBUtil.translate(copy)
    }

    fun translateList(path: String, player: OfflinePlayer? = null, section: ConfigurationSection = language): List<String> {
        val list = section.getStringList(path)
        return when {
            list.isEmpty() -> section.getString(path)?.let {
                translateList(it.split("\\\\n|\\n"), player)
            } ?: return emptyList()
            else -> translateList(list, player)
        }
    }

    fun translateList(list: List<String>, player: OfflinePlayer? = null): List<String> {
        val result: MutableList<String> = ArrayList()
        list.forEach { result.addAll(translateString(it, player).split("\\\\n|\\n")) }
        return result
    }
}