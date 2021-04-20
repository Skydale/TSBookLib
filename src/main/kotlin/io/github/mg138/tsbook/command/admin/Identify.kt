package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.item.util.ItemUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object Identify {
    fun call(sender: CommandSender): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }

        val item = sender.inventory.itemInMainHand
        ItemUtil.checkItem(item) {
            CommandError.handEmpty(sender)
            return false
        }
        val type = ItemUtil.getIdentifier(item) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }
        val id = ItemUtil.getStringTag(item, type.identifier) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }

        val inst = identify(id, type)
        item.amount--
        sender.inventory.addItem(inst.createItem())
        return true
    }
}