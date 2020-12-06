package io.github.twilight_book.Command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;

public class Help extends CommandAbstract {
    public boolean call(CommandSender sender) {
        sender.sendMessage(Book.c.getLang().translate("messages.help"));
        return true;
    }
}
