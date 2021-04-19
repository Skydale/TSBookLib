package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.item.attribute.DefaultIdentifier
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.command.util.CommandUtil
import org.bukkit.command.CommandSender

object Give {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.commands.feedback.give)
        return true
    }

    fun call(sender: CommandSender, playerName: String, itemName: String): Boolean {
        val receiver = CommandUtil.getPlayerByName(playerName, sender) ?: return false

        val item = CommandUtil.getItemByName(itemName, sender) ?: return false
        CommandUtil.sendGetMessage(receiver, sender, itemName)
        receiver.inventory.addItem(CommandUtil.getItem(DefaultIdentifier.ITEM, item))
        return true
    }
}