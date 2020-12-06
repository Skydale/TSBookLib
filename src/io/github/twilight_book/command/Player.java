package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandSender;

public class Player extends CommandAbstract{
    @Override
    public boolean call(CommandSender sender) {
        sender.sendMessage(Book.c.getLang().translate("messages.player"));
        return true;
    }
}
