package io.github.mg138.tsbook.util

import io.github.mg138.tsbook.util.RGBUtil.translateColor
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import java.util.*

object Translate {
    fun getUnsafe(path: String, player: OfflinePlayer? = null, section: ConfigurationSection): String? {
        section.getString(path)?.let {
            return translateString(it, player)
        } ?: return null
    }

    fun get(path: String, player: OfflinePlayer? = null, section: ConfigurationSection): String {
        return getUnsafe(path, player, section) ?: throw IllegalArgumentException("No value set for $path")
    }

    fun translateString(string: String, player: OfflinePlayer? = null) = string.setPlaceholder(player).translateColor()

    fun getList(path: String, player: OfflinePlayer? = null, section: ConfigurationSection): List<String> {
        val list = section.getStringList(path)
        return when {
            list.isEmpty() -> section.getString(path).let {
                return when (it) {
                    null -> emptyList()
                    else -> translateList(it.split("\\n", "\n"), player)
                }
            }
            else -> translateList(list, player)
        }
    }

    fun translateList(list: List<String>, player: OfflinePlayer? = null): List<String> {
        val result: MutableList<String> = ArrayList()
        list.forEach { result.addAll(translateString(it, player).split("\\n", "\n")) }
        return result
    }

    fun String.setPlaceholder(player: OfflinePlayer?) = PlaceholderAPI.setPlaceholders(player, this)
}