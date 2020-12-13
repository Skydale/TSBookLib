package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemTemplate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Get {
	public static boolean call(CommandSender sender, String s) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
			return false;
		}

		if(Book.getCfg().getItemByID(s) != null){
			Commands.setITEM(s);
			sender.sendMessage(Book.getCfg().getLang().translate("messages.get", (Player) sender));
			((Player) sender).getInventory().addItem(getItem(s));
			return true;
		}
		sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
		return false;
	}
	public static boolean call(CommandSender sender){
		sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.get"));
		return true;
	}

	private static ItemStack getItem(String i) {
		return new ItemTemplate(Material.getMaterial(Book.getCfg().getItemByID(i).getString("MATERIAL")), Book.getCfg(), i).getItem();
	}
}