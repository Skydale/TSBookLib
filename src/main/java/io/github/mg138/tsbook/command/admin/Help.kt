package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object Help {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.translate.translate("messages.help"))
        return true
    }
}