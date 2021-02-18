package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminCommands implements CommandExecutor {
    protected static String ITEM;

    public static String getItem() {
        return ITEM;
    }

    public static void setItem(String s) {
        ITEM = s;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        ITEM = null;

        switch (args.length) {
            case 0:
                sender.sendMessage(Book.Companion.getCfg().translate.translate("messages.help"));
                return true;

            case 1:
                switch (args[0].toLowerCase()) {
                    case "reload":
                        return Reload.call(sender);
                    case "help":
                        return Help.call(sender);
                    case "get":
                        return Get.call(sender);
                    case "give":
                        return Give.call(sender);
                    case "unid":
                        return Unidentified.call(sender);
                    case "identify":
                        return Identify.call(sender);
                    case "effect":
                        return Effect.call(sender);
                }

            case 2:
                switch (args[0].toLowerCase()) {
                    case "get":
                        return Get.call(sender, args[1]);
                    case "give":
                        return Give.call(sender);
                    case "unid":
                        return Unidentified.call(sender);
                    case "effect":
                        return Effect.call(sender);
                }
            case 3:
                switch (args[0].toLowerCase()) {
                    case "give":
                        return Give.call(sender, args[1], args[2]);
                    case "unid":
                        return Unidentified.call(sender, args[1], args[2]);
                    case "effect":
                        if (args[2].equals("clear")) return Effect.clear(sender, args[1]);
                        return Effect.call(sender);
                }
            case 4:
                switch (args[0].toLowerCase()) {
                    case "effect":
                        return Effect.call(sender);
                }
            case 5:
                switch (args[0].toLowerCase()) {
                    case "effect":
                        return Effect.call(sender, args[1], args[2], args[3], args[4]);
                }
        }

        sender.sendMessage(Book.Companion.getCfg().translate.translate("errors.unknown_command"));
        return false;
    }
}
