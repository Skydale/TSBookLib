package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemTemplate;
import io.github.twilight_book.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give {
    protected static ItemStack getItem(String s) {
        return new ItemInstance(s).createItem();
    }

    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.give"));
        return true;
    }

    public static boolean call(CommandSender sender, String p, String i) {
        Player player = Book.getInst().getServer().getPlayer(p);
        if (player == null) {
            sender.sendMessage(Book.getCfg().getLang().translate("errors.player_not_found"));
            return false;
        }

        YamlConfiguration item = Book.getCfg().getItemByID(i);
        if (item != null) {
            Commands.setITEM(i);
            sender.sendMessage(Book.getCfg().getLang().translate("messages.get", player));
            player.sendMessage(Book.getCfg().getLang().translate("messages.get", player));
            player.getInventory().addItem(getItem(i));
            return true;
        }

        sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
        return false;
    }
}