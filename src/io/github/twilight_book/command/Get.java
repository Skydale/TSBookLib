package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Get extends Give {
	public static boolean call(CommandSender sender) {
		sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.get"));
		return true;
	}

	public static boolean call(CommandSender sender, String s) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
			return false;
		}

		if (Book.getCfg().getItemByID(s) != null) {
			Commands.setITEM(s);
			sender.sendMessage(Book.getCfg().getLang().translate("messages.get", (Player) sender));
			((Player) sender).getInventory().addItem(getItem(s));
			return true;
		}

		sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
		return false;
	}
}