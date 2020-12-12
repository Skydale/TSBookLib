package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandImplement implements CommandExecutor {
	protected static String cmd;
	protected static Player player;
	protected static String item;

	public static String getCommand() {
		return cmd;
	}

	public static Player getPlayer() {
		return player;
	}

	public static String getItem() {
		return item;
	}

	public static void setItem(String s) { item = s; }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
        	player = (Player) sender;
        } else player = null;
		
        if(args.length > 0){ cmd = args[0]; } else { cmd = ""; }
        item = null;

		if (command.getName().equalsIgnoreCase("tsbook")) {
			if (args.length == 0) {
				sender.sendMessage(Book.c.getLang().translate("messages.help"));
				return true;
			}

			if (args.length == 1) {
				switch (args[0].toLowerCase()) {
					case "reload": return Reload.call(sender);
					case "player": return io.github.twilight_book.command.Player.call(sender);
					case "help":   return Help.call(sender);
					case "get":	   return Get.call(sender);
				}
			}

			if(args.length == 2) {
				if ("get".equals(args[0].toLowerCase())) {
					return Get.call(sender, args[1]);
				}
			}

			sender.sendMessage(Book.c.getLang().translate("errors.unknown_command"));
			return false;
		}

		return false;
	}
}
