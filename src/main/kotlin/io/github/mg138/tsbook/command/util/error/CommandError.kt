package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object CommandError {
    fun handEmpty(sender: CommandSender) {
        sender.sendMessage(BookConfig.translate.translate("errors.hand_empty"))
    }

    fun shouldPutNumber(sender: CommandSender) {
        sender.sendMessage(BookConfig.translate.translate("errors.should_put_number"))
    }

    fun itemNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.translate.translate("errors.item_not_found"))
    }

    fun playerNotFound(sender: CommandSender) {
        sender.sendMessage(BookConfig.translate.translate("errors.player_not_found"))
    }

    fun playerOnly(sender: CommandSender) {
        sender.sendMessage(BookConfig.translate.translate("errors.player_only"))
    }
}