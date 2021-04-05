package io.github.mg138.tsbook.util.papi

import io.github.mg138.tsbook.command.admin.AdminCommands
import org.bukkit.plugin.java.JavaPlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.entity.Player

class PlaceholderExpansionTSBook(private val plugin: JavaPlugin) : PlaceholderExpansion() {
    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return plugin.description.authors.toString()
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun getIdentifier(): String {
        return "tsbook"
    }

    override fun onPlaceholderRequest(player: Player?, identifier: String): String {
        return when (identifier) {
            "item" -> AdminCommands.item!!
            else -> BookConfig.translate.get(identifier)
        }
    }
}