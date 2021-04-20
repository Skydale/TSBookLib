package io.github.mg138.tsbook.command.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.IllegalArgumentException
import java.util.*

object CommandUtil {
    fun sendGetMessage(receiver: CommandSender, sender: CommandSender, itemName: String) {
        val message = getGetMessage(receiver.name, itemName)
        sender.sendMessage(message)
        receiver.sendMessage(message)
    }

    fun getGetMessage(playerName: String, itemName: String) = BookConfig.language.messages.get
        .replace("[!player]", playerName)
        .replace("[!item]", itemName)

    fun getItem(identifier: DefaultIdentifier, setting: ItemSetting): ItemStack {
        val inst = ItemBase(
            setting,
            IdentifiedStat.create(
                setting,
                false
            ),
            UUID.randomUUID()
        )
        return inst.createItem()
    }

    fun getUnidByName(itemName: String, sender: CommandSender): UnidentifiedSetting? {
        return try {
            ItemConfig.getUnid(itemName)
        } catch (e: IllegalArgumentException) {
            CommandError.itemNotFound(sender)
            null
        }
    }

    fun getItemByName(itemName: String, sender: CommandSender): ItemSetting? {
        return try {
            ItemConfig.getItem(itemName)
        } catch (e: IllegalArgumentException) {
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