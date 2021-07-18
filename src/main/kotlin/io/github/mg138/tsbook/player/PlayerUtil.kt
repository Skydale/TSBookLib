package io.github.mg138.tsbook.player

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.command.util.error.CommandError
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun String.toPlayer(sender: CommandSender): Player? {
    return Book.inst.server.getPlayer(this) ?: run {
        CommandError.playerNotFound(sender)
        return null
    }
}