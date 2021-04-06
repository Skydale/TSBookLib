package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object CommandError {
    fun handEmpty(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.hand_empty"))
    }

    fun noSuchOption(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.no_such_option"))
    }

    fun shouldPutNumber(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.should_put_number"))
    }

    fun itemNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.item_not_found"))
    }

    fun playerNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.player_not_found"))
    }

    fun playerOnly(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.player_only"))
    }
}