package io.github.mg138.tsbook.command.util.error

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.CommandSender

object CommandEffectError {
    fun noActiveEffect(sender: CommandSender) {
        sender.sendMessage(BookConfig.language.get("errors.effect.no_active_effect"))
    }
}