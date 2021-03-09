package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.config.item.element.ItemSetting;
import io.github.mg138.tsbook.items.ItemStats;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Give {
    public static ItemStack getItem(String internalType, YamlConfiguration setting) {
        ItemStats stats;
        ConfigurationSection statSetting = setting.getConfigurationSection("stats");
        if (statSetting == null) stats = null; else stats = new ItemStats(
                ItemIdentification.create(setting, false),
                Book.Companion.getCfg()
        );

        ItemInstance inst = new ItemInstance(
                new ItemSetting(setting),
                stats,
                internalType,
                UUID.randomUUID()
        );

        return inst.createItem(Book.inst);
    }

    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.Companion.getCfg().translate.translate("commands.feedback.give"));
        return true;
    }

    public static boolean call(CommandSender sender, String playerName, String itemName) {
        Player player = Book.inst.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_not_found"));
            return false;
        }

        YamlConfiguration item = Book.Companion.getCfg().getItemConfig().getItemByID(itemName);
        if (item != null) {
            AdminCommands.setItem(itemName);
            sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.sendMessage(Book.Companion.getCfg().translate.translate("messages.get", player));
            player.getInventory().addItem(getItem("item", item));
            return true;
        }

        sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.item_not_found"));
        return false;
    }
}