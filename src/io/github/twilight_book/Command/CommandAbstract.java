package io.github.twilight_book.Command;

import org.bukkit.command.CommandSender;

public abstract class CommandAbstract {
	protected String CMD_NAME;

    public boolean call(CommandSender sender) {
    	sender.sendMessage("commands.feedback." + CMD_NAME);
    	return true;
	}
}
