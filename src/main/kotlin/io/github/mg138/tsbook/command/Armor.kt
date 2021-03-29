package io.github.mg138.tsbook.command

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Armor : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(BookConfig.translate.translate("errors.player_only"))
            return false
        }
        Book.equipmentGUIHandler.openEquipmentGUI(sender)
        return true
    }
}