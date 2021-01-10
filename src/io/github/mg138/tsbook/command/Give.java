package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.utils.config.ConfigAbstract;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give {
    protected static ItemStack getItem(ConfigAbstract config, String type, YamlConfiguration setting) {
        ItemInstance inst = null;
        switch (type.toLowerCase()) {
            case "item":
                inst = new ItemInstance(config, setting, ItemIdentification.create(setting, false));
                break;
            case "unid":
                inst = new ItemInstance(config, setting, null);
                break;
        }
        if (inst == null) return null;
        return inst.createItem(Book.getInst(), type);
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
            player.getInventory().addItem(getItem(Book.getCfg(), "item", item));
            return true;
        }

        sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
        return false;
    }
}