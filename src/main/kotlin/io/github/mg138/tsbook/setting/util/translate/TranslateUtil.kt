package io.github.mg138.tsbook.setting.util.translate

import io.github.mg138.tsbook.util.RGBUtil.translateColor
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection

object TranslateUtil {
    fun translateString(
        string: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ) = string.setPlaceholder(player).translateColor().applyPlaceholders(placeholders)

    fun getUnsafe(
        path: String,
        player: OfflinePlayer? = null,
        section: ConfigurationSection,
        placeholders: Map<String, String> = emptyMap()
    ) = section.getString(path)?.let { translateString(it, player, placeholders) }

    fun get(
        path: String,
        player: OfflinePlayer? = null,
        section: ConfigurationSection,
        placeholders: Map<String, String> = emptyMap()
    ) = getUnsafe(path, player, section, placeholders) ?: throw IllegalArgumentException("No value set for $path")

    fun translateList(
        list: List<String>,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ): List<String> {
        val result: MutableList<String> = ArrayList()
        list.forEach { result.addAll(translateString(it, player, placeholders).split("\\n", "\n")) }
        return result
    }

    fun getList(
        path: String,
        player: OfflinePlayer? = null,
        section: ConfigurationSection,
        placeholders: Map<String, String> = emptyMap()
    ): List<String> {
        val list = section.getStringList(path)
        return when {
            list.isEmpty() -> return section.getString(path)?.let {
                translateList(it.split("\\n", "\n"), player, placeholders)
            } ?: emptyList()
            else -> translateList(list, player, placeholders)
        }
    }

    fun String.setPlaceholder(player: OfflinePlayer? = null) = PlaceholderAPI.setPlaceholders(player, this)
    private fun String.applyPlaceholders(placeholders: Map<String, String>): String {
        var out = this
        placeholders.forEach { (placeholder, that) -> out = out.replace(placeholder, that) }
        return out
    }
}