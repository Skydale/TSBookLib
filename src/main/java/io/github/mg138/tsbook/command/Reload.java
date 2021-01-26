package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandSender;

public class Reload {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().getTranslate().translate("messages.reload"));
        Book.getInst().unload();
        Book.getInst().load();
        sender.sendMessage(Book.getCfg().getTranslate().translate("messages.reloaded"));
        return true;
    }
}