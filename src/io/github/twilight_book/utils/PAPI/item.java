package io.github.twilight_book.utils.PAPI;

import io.github.twilight_book.Book;
import io.github.twilight_book.utils.config.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class item extends PlaceholderExpansion {
    JavaPlugin plugin;
    Config config;

    public item(JavaPlugin p, Config c){
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
    public String getAuthor(){ return plugin.getDescription().getAuthors().toString(); }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getIdentifier(){
        return "item";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        return config.getItemByID(identifier.replaceAll("\\..*", "")).getString(identifier.replaceAll(".*?\\.", ""));
    }
}