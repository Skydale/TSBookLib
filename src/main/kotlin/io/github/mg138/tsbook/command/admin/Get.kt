package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.attribute.InternalItemType
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.command.util.CommandUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Get {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.commands.feedback.get)
        return true
    }

    fun call(sender: CommandSender, itemName: String): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }

        val item = CommandUtil.getItemByName(itemName, sender) ?: return false
        sender.sendMessage(CommandUtil.getGetMessage(sender.name, itemName))
        sender.inventory.addItem(CommandUtil.getItem(InternalItemType.ITEM, item))
        return true
    }
}