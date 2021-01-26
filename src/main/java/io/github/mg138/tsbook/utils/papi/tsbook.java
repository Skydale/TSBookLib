package io.github.mg138.tsbook.utils.papi;

import io.github.mg138.tsbook.command.Commands;
import io.github.mg138.tsbook.utils.config.Config;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class tsbook extends PlaceholderExpansion {
    final JavaPlugin plugin;
    final Config config;

    public tsbook(JavaPlugin p, Config c) {
        plugin = p;
        config = c;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "tsbook";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        switch (identifier) {
            case "command":
                return Commands.getCommand();
            case "player":
                if (player == null) return "PLAYER_NOT_FOUND";
                return player.getName();
            case "item":
                return Commands.getITEM();
            default:
                return config.getTranslate().translate(identifier);
        }
    }
}