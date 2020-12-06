package io.github.twilight_book;

import java.io.File;

import io.github.twilight_book.Command.CommandImplement;
import io.github.twilight_book.Command.CommandTabImplement;
import io.github.twilight_book.Utils.Config.Config;
import io.github.twilight_book.Utils.PAPI.tsbook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Book extends JavaPlugin implements Listener {
    private static Book i;
    private static File jar;
    public static Config c;

    @Override
    public void onEnable(){
        i = this;
        jar = getFile();

        reg();
        load();
    }

    @Override
    public void onDisable(){
    }

    public static void load() {
        c.setup(i, jar);
    }

    private void reg() {
        Bukkit.getPluginManager().registerEvents(this, this);
        new tsbook().register();

        getCommand("tsbook").setExecutor(new CommandImplement());
        getCommand("tsbook").setTabCompleter(new CommandTabImplement());
    }
}