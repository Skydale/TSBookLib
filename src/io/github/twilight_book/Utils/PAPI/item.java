package io.github.twilight_book.Utils.PAPI;

import io.github.twilight_book.Book;
import io.github.twilight_book.Command.CommandImplement;
import io.github.twilight_book.Utils.Config.ConfigAbstract;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class item extends PlaceholderExpansion {
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
        return "item";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        switch (identifier) {
            case "name": return
            default: return null;
        }
    }
}