package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.*;

import io.github.mg138.tsbook.config.Config;
import io.github.mg138.tsbook.config.item.element.ItemSetting;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Identify {
    public static boolean call(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.player_only"));
            return false;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(Book.Companion.getCfg().translate.translate("errors.hand_empty"));
            return false;
        }

        String type = ItemUtils.getInternalItemType(item);
        String ID = ItemUtils.getDataTag(Book.inst, item, type);
        if (ID == null || type == null) {
            player.sendMessage(Book.Companion.getCfg().translate.translate("errors.item_not_found"));
            return false;
        }

        ItemInstance inst = identify(Book.Companion.getCfg(), ID, type);
        item.setAmount(item.getAmount() - 1);
        player.getInventory().addItem(inst.createItem(Book.inst));
        return true;
    }

    public static ItemInstance identify(Config config, String ID, String type) {
        ItemSetting setting;
        if (type.equalsIgnoreCase("unid")) {
            List<String> items = Book.Companion.getCfg().itemConfig.getUnidentifiedByID(ID).iden;
            setting = config.itemConfig.getItemByID(items.get(new Random().nextInt(items.size())));
        } else {
            setting = config.itemConfig.getItemByID(ID);
        }

        return new ItemInstance(
                setting,
                new ItemStats(
                        new ItemIdentification(setting, true),
                        Book.Companion.getCfg()
                ),
                "item",
                UUID.randomUUID()
        );
    }
}