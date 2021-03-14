package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book.Companion.setting
import io.github.mg138.tsbook.command.util.CommandUtil
import io.github.mg138.tsbook.command.util.error.CommandError
import org.bukkit.command.CommandSender

object Unidentified {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(setting.translate.translate("commands.feedback.unid"))
        return true
    }

    fun call(sender: CommandSender, playerName: String, itemName: String): Boolean {
        val player = CommandUtil.getPlayerByName(playerName, sender) ?: return false

        val unid = setting.itemConfig.getUnidentifiedByID(itemName) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }
        AdminCommands.item = itemName
        sender.sendMessage(setting.translate.translate("messages.get", player))
        player.sendMessage(setting.translate.translate("messages.get", player))
        player.inventory.addItem(CommandUtil.getItem("unid", unid))
        return true
    }
}