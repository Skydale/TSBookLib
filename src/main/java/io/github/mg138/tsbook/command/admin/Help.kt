package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book.Companion.setting
import org.bukkit.command.CommandSender

object Help {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(setting.translate.translate("messages.help"))
        return true
    }
}