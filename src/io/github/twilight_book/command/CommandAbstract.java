package io.github.twilight_book.command;

import org.bukkit.command.CommandSender;

public abstract class CommandAbstract {
	protected static String CMD_NAME;

    public static boolean call(CommandSender sender) {
    	sender.sendMessage("commands.feedback." + CMD_NAME);
    	return true;
	}
}
