package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.config.BookConfig
import io.github.mg138.tsbook.util.RGBUtil.toChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object DebugMode {
    enum class DebugOption {
        ON_DAMAGE;

        companion object {
            val names = values().map { it.name }
        }
    }

    private val defaultValue = EnumSet.noneOf(DebugOption::class.java)

    private val debugOptions: MutableMap<Player, MutableSet<DebugOption>> = HashMap()

    operator fun get(player: Player) = this.debugOptions.computeIfAbsent(player) { this.defaultValue }

    fun add(player: Player, option: DebugOption) = this[player].add(option)

    fun has(player: Player, option: DebugOption) = this[player].contains(option)

    fun remove(player: Player, option: DebugOption) = this[player].remove(option)

    fun remove(player: Player) = this[player].clear()

    fun toggle(player: Player, option: DebugOption) {
        if (this.remove(player, option)) {
            player.sendMessage("&9[Debug] &eDeactivated Debug Option: &f$option".toChatColor())
        } else {
            this.add(player, option)
            player.sendMessage("&9[Debug] &eActivated Debug Option: &f$option".toChatColor())
        }
    }

    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.commands.feedback.debug)
        return true
    }

    fun call(sender: CommandSender, string: String): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }

        if (string == "clear") {
            this[sender].let {
                return if (it.isEmpty()) {
                    sender.sendMessage("&9[Debug] &eDeactivated &fNothing.".toChatColor())
                    false
                } else {
                    sender.sendMessage("&9[Debug] &eDeactivated Debug Options of &f${it}".toChatColor())
                    // message is sent before actually removing the options,
                    // because otherwise the list would be empty when the message is sent
                    this.remove(sender)
                    true
                }
            }
        }

        return try {
            this.toggle(sender, DebugOption.valueOf(string))
            true
        } catch (e: IllegalArgumentException) {
            CommandError.noSuchOption(sender)
            false
        }
    }
}

