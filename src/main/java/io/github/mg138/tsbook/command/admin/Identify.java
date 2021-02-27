package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.items.ItemUtils;

import io.github.mg138.tsbook.utils.config.Config;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
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
        if (type.equalsIgnoreCase("unid")) {
            List<String> items = Book.Companion.getCfg().getItemConfig().getUnidentifiedByID(ID).getStringList("ITEMS");
            YamlConfiguration setting = config.getItemConfig().getItemByID(items.get(new Random().nextInt(items.size())));
            return new ItemInstance(config, setting, ItemIdentification.create(setting, true), "item", UUID.randomUUID());
        }
        YamlConfiguration setting = config.getItemConfig().getItemByID(ID);
        return new ItemInstance(config, setting, ItemIdentification.create(setting, true), "item", UUID.randomUUID());
    }
}
