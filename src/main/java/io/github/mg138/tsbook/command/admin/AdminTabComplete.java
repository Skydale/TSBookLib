package io.github.mg138.tsbook.command.admin;

import io.github.mg138.tsbook.Book;

import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
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

public class AdminTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("tsbook.test")) {
            List<String> result = new ArrayList<>();

            switch (args.length) {
                case 1:
                    StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "player", "get", "help", "give", "unid", "identify", "effect"), result);
                    break;

                case 2:
                    switch (args[0].toLowerCase()) {
                        case "get":
                            StringUtil.copyPartialMatches(args[1], Book.Companion.getCfg().getItemConfig().getItems(), result);
                            break;
                        case "give":
                        case "unid":
                        case "effect":
                            StringUtil.copyPartialMatches(args[1], getPlayerNames(), result);
                            break;
                    }
                    break;
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "give":
                            StringUtil.copyPartialMatches(args[2], Book.Companion.getCfg().getItemConfig().getItems(), result);
                            break;
                        case "unid":
                            StringUtil.copyPartialMatches(args[2], Book.Companion.getCfg().getItemConfig().getUnidentified(), result);
                            break;
                        case "effect":
                            List<String> matcher = new ArrayList<>(StatusEffectType.names);
                            matcher.add("clear");
                            StringUtil.copyPartialMatches(args[2], matcher, result);
                    }
                    break;
                case 4:
                    switch (args[0].toLowerCase()) {
                        case "effect":
                            result.add("<power>");
                    }
                    break;
                case 5:
                    switch (args[0].toLowerCase()) {
                        case "effect":
                            result.add("<ticks>");
                    }
                    break;
            }
            return result;
        }
        return Collections.emptyList();
    }

    protected List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Book.inst.getServer().getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}