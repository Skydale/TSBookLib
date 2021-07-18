package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.command.CommandSender

object Reload {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.messages.reload)
        Book.inst.unload()
        Book.inst.load()
        sender.sendMessage(BookConfig.language.messages.reloaded)
        return true
    }
}