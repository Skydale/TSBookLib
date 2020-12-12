package io.github.twilight_book.utils.PAPI;

import io.github.twilight_book.Book;
import io.github.twilight_book.command.CommandImplement;
import io.github.twilight_book.utils.config.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class tsbook extends PlaceholderExpansion {
    JavaPlugin plugin;
    Config config;

    public tsbook(JavaPlugin p, Config c){
        plugin = p;
        config = c;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getIdentifier(){
        return "tsbook";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        switch (identifier) {
            case "command":
                return CommandImplement.getCommand();
            case "player":
                if(player == null) return "PLAYER_NOT_FOUND";
                return player.getName();
            case "item":
                return CommandImplement.getItem();
            default:
                return config.getLang().translate(identifier);
        }
    }
}