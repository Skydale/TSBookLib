package io.github.mg138.tsbook.command

import io.github.mg138.tsbook.listener.event.inventory.EquipmentGUIHandler
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Armor : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(BookConfig.translate.get("errors.player_only"))
            return false
        }
        EquipmentGUIHandler.openEquipmentGUI(sender)
        return true
    }
}