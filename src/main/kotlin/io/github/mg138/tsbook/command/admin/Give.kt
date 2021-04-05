package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.command.util.CommandUtil
import org.bukkit.command.CommandSender

object Give {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.translate.get("commands.feedback.give"))
        return true
    }

    fun call(sender: CommandSender, playerName: String, itemName: String): Boolean {
        val player = CommandUtil.getPlayerByName(playerName, sender) ?: return false
        val item = CommandUtil.getItemByName(itemName, sender) ?: return false

        AdminCommands.item = itemName
        sender.sendMessage(BookConfig.translate.get("messages.get", player))
        player.sendMessage(BookConfig.translate.get("messages.get", player))
        player.inventory.addItem(CommandUtil.getItem("item", item))
        return true
    }
}