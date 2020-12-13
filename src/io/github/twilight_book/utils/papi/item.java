package io.github.twilight_book.utils.papi;

import io.github.twilight_book.utils.config.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class item extends PlaceholderExpansion {
    final JavaPlugin plugin;
    final Config config;

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

    @NotNull
    @Override
    public String getAuthor(){ return plugin.getDescription().getAuthors().toString(); }

    @NotNull
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @NotNull
    @Override
    public String getIdentifier(){
        return "item";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        return config.getItemByID(identifier.replaceAll("\\..*" , "")).
                        getString(identifier.replaceAll(".*?\\.", ""));
    }
}