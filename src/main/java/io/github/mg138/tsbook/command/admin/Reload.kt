package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object Reload {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.translate.translate("messages.reload"))
        Book.inst.unload()
        Book.inst.load()
        sender.sendMessage(BookConfig.translate.translate("messages.reloaded"))
        return true
    }
}