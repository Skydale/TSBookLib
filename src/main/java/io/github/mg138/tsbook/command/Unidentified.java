package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Unidentified extends Give {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.Companion.getCfg().translate.translate("commands.feedback.unid"));
        return true;
    }

    public static boolean call(CommandSender sender, String playerName, String item) {
        Player player = Book.inst.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_not_found"));
            return false;
        }

        YamlConfiguration unid = Book.Companion.getCfg().getItemConfig().getUnidentifiedByID(item);
        if (unid != null) {
            Commands.setItem(item);
            sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.getInventory().addItem(getItem(Book.Companion.getCfg(), "unid", unid));
            return true;
        }

        sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.item_not_found"));
        return false;
    }
}
