package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap
object DebugMode {
    enum class DebugOption {
        ON_DAMAGE;

        companion object {
            val names = values().map { it.name }
        }
    }

    private val debugOptions: MutableMap<Player, MutableSet<DebugOption>> = HashMap()

    operator fun get(player: Player) = debugOptions[player]

    operator fun set(player: Player, debugOption: DebugOption) {
        this.debugOptions.putIfAbsent(player, EnumSet.noneOf(DebugOption::class.java))

        this.debugOptions[player]!!.add(debugOption)
    }

    fun hasOption(player: Player, debugOption: DebugOption) = this.debugOptions[player]?.contains(debugOption) ?: false

    fun remove(player: Player, debugOption: DebugOption) = debugOptions[player]?.remove(debugOption)
    fun remove(player: Player) = debugOptions.remove(player)

    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.get("commands.feedback.debug"))
        return true
    }

    fun call(sender: CommandSender, string: String): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }

        if (string == "clear") {
            this.remove(sender)
            return true
        }

        return try {
            val debugOption = DebugOption.valueOf(string)
            if (hasOption(sender, debugOption)) {
                this.remove(sender, debugOption)
                sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&9[Debug] &eDeactivated Debug Mode: &f$string"
                    )
                )
            } else {
                this[sender] = debugOption
                sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&9[Debug] &eActivated Debug Mode: &f$string"
                    )
                )
            }
            true
        } catch (e: IllegalArgumentException) {
            CommandError.noSuchOption(sender)
            false
        }
    }
}

