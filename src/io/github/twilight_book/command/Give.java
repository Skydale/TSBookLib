package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemTemplate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give {
    protected static ItemStack getItem(String s) {
        ItemTemplate item = new ItemTemplate(
                Material.getMaterial(Book.getCfg().getItemByID(s).getString("MATERIAL")),
                Book.getCfg(), s
        );
        item.setDataTag(Book.getInst(), "item", s);
        return item.getItem();
    }

    public static boolean call(CommandSender sender) {
        call(sender, null);
        return true;
    }

    public static boolean call(CommandSender sender, String i) {
        sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.give"));
        return true;
    }

    public static boolean call(CommandSender sender, String p, String i) {
        Player player = Book.getInst().getServer().getPlayer(p);
        if (player == null) {
            sender.sendMessage(Book.getCfg().getLang().translate("errors.player_not_found"));
            return false;
        }

        if (Book.getCfg().getItemByID(i) != null) {
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