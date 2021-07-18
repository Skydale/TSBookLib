package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.command.CommandSender

object CommandEffectError {
    fun noActiveEffect(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.errors.effect.noActiveEffect)
    }
}