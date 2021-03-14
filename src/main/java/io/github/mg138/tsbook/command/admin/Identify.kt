package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.Book.Companion.setting
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.setting.BookSetting
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object Identify {
    private val rand = Random()

    fun call(sender: CommandSender): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }

        val item = sender.inventory.itemInMainHand
        val type = ItemUtils.getInternalItemType(item) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }
        val id = ItemUtils.getStringTag(item, type) ?: run {
            CommandError.itemNotFound(sender)
            return false
        }

        val inst = identify(setting, id, type)
        item.amount--
        sender.inventory.addItem(inst.createItem())
        return true
    }

    private fun identify(bookSetting: BookSetting, ID: String, type: String): ItemInstance {
        val setting: ItemSetting = if (type == "unid") {
            val items = setting.itemConfig.getUnidentifiedByID(ID).iden
            bookSetting.itemConfig.getItemByID(items[rand.nextInt(items.size)])
        } else {
            bookSetting.itemConfig.getItemByID(ID)
        }

        return ItemInstance(
            setting,
            ItemStats.create(
                Book.setting,
                setting,
                true
            ),
            "item",
            UUID.randomUUID()
        )
    }
}