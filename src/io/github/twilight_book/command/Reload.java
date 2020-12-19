package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;

public class Reload {
	public static boolean call(CommandSender sender) {
		sender.sendMessage(Book.getCfg().getLang().translate("messages.reload"));
		Book.getInst().unload();
		Book.getInst().load();
		sender.sendMessage(Book.getCfg().getLang().translate("messages.reloaded"));
		return true;
	}
}