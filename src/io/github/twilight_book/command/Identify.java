package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemTemplate;
import io.github.twilight_book.items.ItemUtils;
import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Identify {
    public static boolean call(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
            return false;
        }
        Player player = (Player) sender;

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() == Material.AIR) {
            player.sendMessage(Book.getCfg().getLang().translate("errors.hand_empty"));
            return false;
        }

        String ID = ItemUtils.getDataTag(Book.getInst(), hand, "unid");
        if (ID == null) {
            player.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
            return false;
        }

        ItemTemplate item = identify(Book.getCfg().getUnidentifiedByID(ID), Book.getCfg());
        hand.setAmount(hand.getAmount() - 1);
        player.getInventory().addItem(item.getItem());
        return true;
    }

    public static ItemTemplate identify(YamlConfiguration unidentified, ConfigAbstract config){
        List<String> items = unidentified.getStringList("ITEMS");
        YamlConfiguration itemSetting = config.getItemByID(items.get(new Random().nextInt(items.size())));

        String mat = itemSetting.getString("MATERIAL");
        if (mat == null) throw new IllegalArgumentException("Cannot get Material of the item.");

        return new ItemTemplate(
                Book.getInst(),
                Material.getMaterial(mat),
                config,
                itemSetting
        );
    }
}
