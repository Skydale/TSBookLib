package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemIdentification;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemUtils;
import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class Identify {
    public static boolean call(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
            return false;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(Book.getCfg().getLang().translate("errors.hand_empty"));
            return false;
        }

        String type = ItemUtils.getItemType(item);

        String ID = ItemUtils.getDataTag(Book.getInst(), item, type);
        if (ID == null) {
            player.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
            return false;
        }

        ItemInstance inst = identify(Book.getCfg(), ID, type);
        item.setAmount(item.getAmount() - 1);
        player.getInventory().addItem(inst.createItem(Book.getInst(), "item"));
        return true;
    }

    public static ItemInstance identify(ConfigAbstract config, String ID, String type){
        if (type.equalsIgnoreCase("unid")) {
            List<String> items = Book.getCfg().getUnidentifiedByID(ID).getStringList("ITEMS");
            YamlConfiguration setting = config.getItemByID(items.get(new Random().nextInt(items.size())));
            return new ItemInstance(config, setting, new ItemIdentification(setting, true));
        }
        YamlConfiguration setting = config.getItemByID(ID);
        return new ItemInstance(config, setting, new ItemIdentification(setting, true));
    }
}
