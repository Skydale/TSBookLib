package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Armor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_only"));
            return false;
        }
        Player player = (Player) sender;
        Book.equipmentGUIHandler.openEquipmentGUI(player);
        return true;
    }
}
