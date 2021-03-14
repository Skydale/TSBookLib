package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.Book.Companion.setting
import org.bukkit.command.CommandSender

object Reload {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(setting.translate.translate("messages.reload"))
        Book.inst.unload()
        Book.inst.load()
        sender.sendMessage(setting.translate.translate("messages.reloaded"))
        return true
    }
}