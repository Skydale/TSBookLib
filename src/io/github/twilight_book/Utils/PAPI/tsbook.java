package io.github.twilight_book.Utils.PAPI;

import io.github.twilight_book.Book;
import io.github.twilight_book.Command.CommandImplement;
import io.github.twilight_book.Utils.Config.ConfigAbstract;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class tsbook extends PlaceholderExpansion {
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
        return Book.c.getPlugin().getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion(){
        return Book.c.getPlugin().getDescription().getVersion();
    }

    @Override
    public String getIdentifier(){
        return "tsbook";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        switch (identifier) {
            case "prefix":
                return Book.c.getLang().translate("prefix");
            case "command":
                return CommandImplement.getCommand();
            case "player":
                if(player == null) return "PLAYER_NOT_FOUND";
                return player.getName();
            default:
                return null;
        }
    }
}