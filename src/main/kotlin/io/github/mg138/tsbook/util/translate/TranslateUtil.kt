package io.github.mg138.tsbook.util.translate

import io.github.mg138.tsbook.util.RGBUtil.translateColor
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import java.util.*

object TranslateUtil {
    val defaultErrorMsg = { it: ConfigurationSection, path: String ->
        "${it.name} doesn't contain $path"
    }

    private fun String.applyPlaceholders(placeholders: Map<String, String>): String {
        var out = this
        placeholders.forEach { (placeholder, that) -> out = out.replace(placeholder, that) }
        return out
    }

    private fun String.splitLines() = this.split("\\n", "\n")

    fun String.setPlaceholder(player: OfflinePlayer? = null) = PlaceholderAPI.setPlaceholders(player, this)

    fun translateString(
        string: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ) = string
        .setPlaceholder(player)
        .translateColor()
        .applyPlaceholders(placeholders)

    fun translateList(
        list: List<String>,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ): List<String> {
        val result: MutableList<String> = LinkedList()
        list.forEach { result.addAll(translateString(it, player, placeholders).splitLines()) }
        return result
    }

    fun get(
        section: ConfigurationSection,
        path: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ) = section.getString(path)?.let { translateString(it, player, placeholders) }

    fun get(
        section: ConfigurationSection,
        path: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap(),
        action: (String) -> Unit
    ) = this.get(section, path, player, placeholders)?.let(action)

    fun getSafe(
        section: ConfigurationSection,
        path: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap(),
        errorMessage: (ConfigurationSection, String) -> String = defaultErrorMsg
    ) = this.get(section, path, player, placeholders) ?: throw IllegalArgumentException(errorMessage(section, path))

    fun getSafe(
        section: ConfigurationSection,
        path: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap(),
        action: (String) -> Unit
    ) = this.getSafe(section, path, player, placeholders).let(action)

    fun getList(
        section: ConfigurationSection,
        path: String,
        player: OfflinePlayer? = null,
        placeholders: Map<String, String> = emptyMap()
    ): List<String> {
        val list = section.getStringList(path)
        if (list.isNotEmpty()) return translateList(list, player, placeholders)

        return section.getString(path)?.let {
            translateList(it.splitLines(), player, placeholders)
        } ?: emptyList()
    }
}