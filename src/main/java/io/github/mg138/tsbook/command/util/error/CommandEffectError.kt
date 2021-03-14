package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.Book
import org.bukkit.command.CommandSender

object CommandEffectError {
    fun noActiveEffect(sender: CommandSender) {
        sender.sendMessage(Book.setting.translate.translate("errors.effect.no_active_effect"))
    }
}