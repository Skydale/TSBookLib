package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil
import io.github.mg138.tsbook.command.util.CommandUtil
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import io.github.mg138.tsbook.setting.item.ItemConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.ArrayList

class AdminTabComplete : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        if (sender.hasPermission("tsbook.test")) {
            val result: MutableList<String> = ArrayList()
            val category = args[0].toLowerCase()

            when (args.size) {
                1 -> StringUtil.copyPartialMatches(
                    args[0],
                    listOf("reload", "player", "get", "help", "give", "unid", "identify", "effect"),
                    result
                )
                2 -> when (category) {
                    "get" -> StringUtil.copyPartialMatches(
                        args[1], ItemConfig.getItems(), result
                    )
                    "give", "unid", "effect" -> StringUtil.copyPartialMatches(
                        args[1],
                        CommandUtil.getPlayerNames(Book.inst.server.onlinePlayers),
                        result
                    )
                }
                3 -> when (category) {
                    "give" -> StringUtil.copyPartialMatches(
                        args[2], ItemConfig.getItems(), result
                    )
                    "unid" -> StringUtil.copyPartialMatches(
                        args[2],
                        ItemConfig.getUnids(),
                        result
                    )
                    "effect" -> {
                        StringUtil.copyPartialMatches(
                            args[2],
                            ArrayList(StatusEffectType.names).also { it.add("clear") },
                            result
                        )
                    }
                }
                4 -> when (category) {
                    "effect" -> result.add("<power>")
                }
                5 -> when (category) {
                    "effect" -> result.add("<ticks>")
                }
            }
            return result
        }
        return emptyList()
    }
}