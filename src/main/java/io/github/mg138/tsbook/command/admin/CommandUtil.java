package io.github.mg138.tsbook.command.admin;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil {
    public static List<String> getPlayerNames(Server server) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : server.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}
