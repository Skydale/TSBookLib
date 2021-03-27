package io.github.mg138.tsbook.command.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

object CommandUtil {
    fun getItem(internalType: String, setting: ItemSetting): ItemStack {
        val inst = ItemInstance(
            setting,
            ItemStats.create(
                setting,
                BookConfig,
                false
            ),
            internalType,
            UUID.randomUUID()
        )
        return inst.createItem()
    }

    fun getItemByName(itemName: String, sender: CommandSender) : ItemSetting? {
        return ItemConfig.getItem(itemName) ?: run {
            CommandError.itemNotFound(sender)
            return null
        }
    }

    fun getPlayerByName(playerName: String, sender: CommandSender): Player? {
        return Book.inst.server.getPlayer(playerName) ?: run {
            CommandError.playerNotFound(sender)
            return null
        }
    }

    fun getPlayerNames(players: Collection<Player>): List<String> {
        val playerNames: MutableList<String> = ArrayList()
        players.forEach {
            playerNames.add(it.name)
        }
        return playerNames
    }
}