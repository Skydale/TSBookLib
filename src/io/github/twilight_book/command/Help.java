package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;

public class Help {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().getLang().translate("messages.help"));
        return true;
    }
}