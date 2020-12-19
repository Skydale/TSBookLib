package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemTemplate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetX {
    protected static ItemStack getItem(String s) {
        ItemInstance item2 = new ItemInstance(s);
        return item2.createItem();
    }

    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.get"));
        return true;
    }

    public static boolean call(CommandSender sender, String s) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
            return false;
        }

        if (Book.getCfg().getItemByID(s) != null) {
            Commands.setITEM(s);
            sender.sendMessage(Book.getCfg().getLang().translate("messages.get", (Player) sender));
            ((Player) sender).getInventory().addItem(getItem(s));
            return true;
        }

        sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
        return false;
    }
}