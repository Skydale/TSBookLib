package io.github.twilight_book.Command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;

public class Reload extends CommandAbstract {
	@Override
	public boolean call(CommandSender sender) {
		sender.sendMessage(Book.c.getLang().translate("messages.reload"));
		Book.load();
		sender.sendMessage(Book.c.getLang().translate("messages.reloaded"));
		return true;
	}
}