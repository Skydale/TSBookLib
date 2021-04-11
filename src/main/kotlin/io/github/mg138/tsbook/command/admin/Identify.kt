package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.attribute.InternalItemType
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.item.ItemStat
import io.github.mg138.tsbook.item.ItemUtils
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
        val id = ItemUtils.getStringTag(item, type.string) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }

        val inst = identify(id, type)
        item.amount--
        sender.inventory.addItem(inst.createItem())
        return true
    }

    private fun identify(ID: String, internalItemType: InternalItemType): ItemInstance {
        val setting = when (internalItemType) {
            InternalItemType.UNID -> ItemConfig.getItem(ItemConfig.getUnid(ID)!!.iden.random())!!
            else -> ItemConfig.getItem(ID)!!
        }

        return ItemInstance(
            setting,
            ItemStat.create(
                setting,
                true
            ),
            InternalItemType.ITEM,
            UUID.randomUUID()
        )
    }
}