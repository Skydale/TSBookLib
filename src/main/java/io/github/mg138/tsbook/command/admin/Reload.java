package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandSender;

public class Reload {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.reload"));
        Book.inst.unload();
        Book.inst.load();
        sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.reloaded"));
        return true;
    }
}