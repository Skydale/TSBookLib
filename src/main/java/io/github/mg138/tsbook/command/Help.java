package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandSender;

public class Help {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().translate.translate("messages.help"));
        return true;
    }
}