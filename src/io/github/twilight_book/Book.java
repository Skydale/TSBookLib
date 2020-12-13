package io.github.twilight_book;

import java.io.File;

import io.github.twilight_book.command.Commands;
import io.github.twilight_book.command.CommandsTab;
import io.github.twilight_book.event.EntityDamage;
import io.github.twilight_book.utils.papi.item;
import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.papi.tsbook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Book extends JavaPlugin implements Listener {
    private static Book i;
    private static File jar;
    private static Config config;
    private static EntityDamage mmhook;

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
        config = new Config();
        config.setup(i, jar);
        mmhook = new EntityDamage();
    }

    private void reg() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        new tsbook(i, config).register();
        new item(i, config).register();

        getCommand("tsbook").setExecutor    (new Commands());
        getCommand("tsbook").setTabCompleter(new CommandsTab());
    }

    public static Config getCfg(){
        return config;
    }
}