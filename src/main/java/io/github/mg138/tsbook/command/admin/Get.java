package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Get extends Give {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.Companion.getCfg().translate.translate("commands.feedback.get"));
        return true;
    }

    public static boolean call(CommandSender sender, String i) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_only"));
            return false;
        }
        Player player = (Player) sender;

        YamlConfiguration item = Book.Companion.getCfg().getItemConfig().getItemByID(i);
        if (item != null) {
            AdminCommands.setItem(i);
            sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", (Player) sender));
            player.getInventory().addItem(getItem(Book.Companion.getCfg(), "item", item));
            return true;
        }

        sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.item_not_found"));
        return false;
    }
}