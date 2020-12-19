package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
	protected static String CMD;
	protected static Player PLAYER;
	protected static String ITEM;

	public static String getCommand() {
		return CMD;
	}

	public static Player getPlayer() {
		return PLAYER;
	}

	public static String getITEM() {
		return ITEM;
	}

	public static void setITEM(String s) {
		ITEM = s;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player) {
			PLAYER = (Player) sender;
		} else PLAYER = null;

		if (args.length > 0) {
			CMD = args[0];
		} else CMD = "";

		ITEM = null;

		if (command.getName().equalsIgnoreCase("tsbook")) {
			switch (args.length) {
				case 0:
					sender.sendMessage(Book.getCfg().getLang().translate("messages.help"));
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
						case "unidentified":
							return Unidentified.call(sender);
						case "identify":
							return Identify.call(sender);
					}

				case 2:
					switch (args[0].toLowerCase()) {
						case "get":
							return Get.call(sender, args[1]);
						case "give":
							return Give.call(sender);
						case "unidentified":
							return Unidentified.call(sender);
					}
				case 3:
					switch (args[0].toLowerCase()) {
						case "give":
							return Give.call(sender, args[1], args[2]);
						case "unidentified":
							return Unidentified.call(sender, args[1], args[2]);
					}
			}

			sender.sendMessage(Book.getCfg().getLang().translate("errors.unknown_command"));
			return false;
		}

		return false;
	}
}
