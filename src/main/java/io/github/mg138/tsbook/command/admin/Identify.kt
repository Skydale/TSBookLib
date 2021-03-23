package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
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
        ItemUtils.checkItem(item) {
            CommandError.handEmpty(sender)
            return false
        }
        val type = ItemUtils.getInternalItemType(item) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }
        val id = ItemUtils.getStringTag(item, type) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }

        val inst = identify(id, type)
        item.amount--
        sender.inventory.addItem(inst.createItem())
        return true
    }

    private fun identify(ID: String, type: String): ItemInstance {
        val setting = if (type == "unid") {
            ItemConfig.getItem(ItemConfig.getUnid(ID)!!.iden.random())!!
        } else {
            ItemConfig.getItem(ID)!!
        }

        return ItemInstance(
            setting,
            ItemStats.create(
                setting,
                BookConfig,
                true
            ),
            "item",
            UUID.randomUUID()
        )
    }
}