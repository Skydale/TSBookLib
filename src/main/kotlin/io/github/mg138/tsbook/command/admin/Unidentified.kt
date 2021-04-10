package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.command.util.CommandUtil
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.setting.item.ItemConfig
import org.bukkit.command.CommandSender

object Unidentified {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.get("commands.feedback.unid"))
        return true
    }

    fun call(sender: CommandSender, playerName: String, itemName: String): Boolean {
        val player = CommandUtil.getPlayerByName(playerName, sender) ?: return false

        val unid = ItemConfig.getUnid(itemName) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }
        AdminCommands.item = itemName
        sender.sendMessage(BookConfig.language.get("messages.get", player))
        player.sendMessage(BookConfig.language.get("messages.get", player))
        player.inventory.addItem(CommandUtil.getItem("unid", unid))
        return true
    }
}