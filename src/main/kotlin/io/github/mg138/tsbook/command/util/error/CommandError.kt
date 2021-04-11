package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object CommandError {
    fun handEmpty(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.handEmpty)
    }

    fun noSuchOption(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.noSuchOption)
    }

    fun shouldPutInteger(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.shouldPutInteger())
    }

    fun itemNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.itemNotFound)
    }

    fun playerNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.playerNotFound)
    }

    fun playerOnly(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.playerOnly)
    }
}