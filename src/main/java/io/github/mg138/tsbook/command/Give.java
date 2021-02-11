package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemInstance;

import io.github.mg138.tsbook.utils.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give {
    protected static ItemStack getItem(Config config, String internalType, YamlConfiguration setting) {
        ItemInstance inst = new ItemInstance(config, setting, ItemIdentification.create(setting, false), internalType);

        return inst.createItem(Book.inst);
    }

    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.Companion.getCfg().translate.translate("commands.feedback.give"));
        return true;
    }

    public static boolean call(CommandSender sender, String p, String i) {
        Player player = Book.inst.getServer().getPlayer(p);
        if (player == null) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_not_found"));
            return false;
        }

        YamlConfiguration item = Book.Companion.getCfg().getItemConfig().getItemByID(i);
        if (item != null) {
            Commands.setItem(i);
            sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.getInventory().addItem(getItem(Book.Companion.getCfg(), "item", item));
            return true;
        }

        sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.item_not_found"));
        return false;
    }
}