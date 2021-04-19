package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.item.attribute.DefaultIdentifier
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.item.util.ItemUtil
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

    private fun identify(id: String, identifier: DefaultIdentifier): ItemInstance {
        val setting = when (identifier) {
            DefaultIdentifier.UNID -> ItemConfig.getItem(ItemConfig.getUnid(id).iden.random())
            else -> ItemConfig.getItem(id)
        }

        return ItemInstance(
            setting,
            IdentifiedStat.create(
                setting,
                true
            ),
            UUID.randomUUID()
        )
    }
}