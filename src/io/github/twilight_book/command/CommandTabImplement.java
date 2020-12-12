package io.github.twilight_book.command;

import io.github.twilight_book.Book;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTabImplement implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("tsbook")) {
			if (sender.hasPermission("tsbook.test")) {
				if (args.length == 1) {
					List<String> s = new ArrayList<>();
					StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "player", "get"), s);
					return s;
				}

				if(args.length == 2) {
					switch(args[0].toLowerCase()) {
						case "get":
							List<String> s = new ArrayList<>();
							StringUtil.copyPartialMatches(args[1], Book.c.getItems(), s);
							return s;
					}
				}
			}
		}
		return Collections.emptyList();
	}
}