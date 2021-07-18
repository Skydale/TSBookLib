package io.github.mg138.tsbook.command.util

import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object CommandUtil {
    fun sendGetMessage(receiver: CommandSender, sender: CommandSender, itemName: String) {
        val message = BookConfig.language.messages.get(receiver.name, itemName)
        sender.sendMessage(message)
        receiver.sendMessage(message)
    }

    fun getPlayerNames(players: Collection<Player>): List<String> {
        val playerNames: MutableList<String> = LinkedList()

        players.forEach {
            playerNames += it.name
        }

        return playerNames
    }
}