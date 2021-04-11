package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object AdminCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val category = when {
            args.isNotEmpty() -> args[0].toLowerCase()
            else -> null
        }

        when (args.size) {
            0 -> {
                return Help.call(sender)
            }
            1 -> when (category) {
                "reload" -> return Reload.call(sender)
                "help" -> return Help.call(sender)
                "get" -> return Get.call(sender)
                "give" -> return Give.call(sender)
                "unid" -> return Unidentified.call(sender)
                "identify" -> return Identify.call(sender)
                "effect" -> return Effect.call(sender)
                "debug" -> return DebugMode.call(sender)
            }
            2 -> when (category) {
                "help" -> return Help.call(sender, args[1])
                "get" -> return Get.call(sender, args[1])
                "give" -> return Give.call(sender)
                "unid" -> return Unidentified.call(sender)
                "effect" -> return Effect.call(sender)
                "debug" -> return DebugMode.call(sender, args[1])
            }
            3 -> when (category) {
                "give" -> return Give.call(sender, args[1], args[2])
                "unid" -> return Unidentified.call(sender, args[1], args[2])
                "effect" -> return when (args[2]) {
                    "clear" -> Effect.clear(sender, args[1])
                    else -> Effect.call(sender)
                }
            }
            4 -> when (category) {
                "effect" -> return Effect.call(sender)
            }
            5 -> when (category) {
                "effect" -> return Effect.call(sender, args[1], args[2], args[3], args[4])
            }
        }
        sender.sendMessage(BookConfig.language.errors.unknownCommand)
        return false
    }
}