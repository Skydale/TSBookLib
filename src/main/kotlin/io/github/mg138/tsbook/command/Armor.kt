package io.github.mg138.tsbook.command

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.listener.event.inventory.EquipmentGUIHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Armor : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            CommandError.playerOnly(sender)
            return false
        }
        EquipmentGUIHandler.openEquipmentGUI(sender)
        return true
    }
}