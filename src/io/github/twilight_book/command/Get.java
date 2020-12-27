package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Get extends Give {
	public static boolean call(CommandSender sender) {
		sender.sendMessage(Book.getCfg().getLang().translate("commands.feedback.get"));
		return true;
	}

	public static boolean call(CommandSender sender, String i) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Book.getCfg().getLang().translate("errors.player_only"));
			return false;
		}

		YamlConfiguration item = Book.getCfg().getItemByID(i);
		if (item != null) {
			Commands.setITEM(i);
			sender.sendMessage(Book.getCfg().getLang().translate("messages.get", (Player) sender));
			((Player) sender).getInventory().addItem(getItem(Book.getCfg(), "item", item));
			return true;
		}

		sender.sendMessage(Book.getCfg().getLang().translate("errors.item_not_found"));
		return false;
	}
}