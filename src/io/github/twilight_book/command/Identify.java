package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemUtils;
import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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

        ItemInstance item = identify(Book.getCfg(), ID);
        hand.setAmount(hand.getAmount() - 1);
        player.getInventory().addItem(item.createItem(Book.getInst()));
        return true;
    }

    public static ItemInstance identify(ConfigAbstract config, String ID){
        List<String> items = Book.getCfg().getUnidentifiedByID(ID).getStringList("ITEMS");

        ItemInstance i = new ItemInstance(config, items.get(new Random().nextInt(items.size())), "item");
        i.reIdentify();
        return i;
    }
}
