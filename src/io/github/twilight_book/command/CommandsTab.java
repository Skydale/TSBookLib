package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandsTab implements TabCompleter {

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, String label, @NotNull String[] args) {
		if(label.equalsIgnoreCase("tsbook")) {
			if (sender.hasPermission("tsbook.test")) {
				List<String> s = new ArrayList<>();

				switch (args.length) {
					case 1:
						StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "player", "get", "help", "give"), s);
						break;

					case 2:
						switch (args[0].toLowerCase()) {
							case "get":
								StringUtil.copyPartialMatches(args[1], Book.getCfg().getItems(), s);
								break;
							case "give":
								StringUtil.copyPartialMatches(args[1], getPlayerNames(), s);
								break;
						}
						break;
					case 3:
						switch (args[0].toLowerCase()) {
							case "give":
								StringUtil.copyPartialMatches(args[2], Book.getCfg().getItems(), s);
								break;
						}
						break;
				}
				return s;
			}
		}
		return Collections.emptyList();
	}

	protected List<String> getPlayerNames(){
		List<String> playerNames = new ArrayList<>();
		for (Player player : Book.getInst().getServer().getOnlinePlayers()){
			playerNames.add(player.getName());
		}
		return playerNames;
	}
}