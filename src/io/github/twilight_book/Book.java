package io.github.twilight_book;

import java.io.File;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.twilight_book.command.Commands;
import io.github.twilight_book.command.CommandsTab;
import io.github.twilight_book.event.DisableHeartParticle;
import io.github.twilight_book.event.EntityDamage;
import io.github.twilight_book.items.ItemPacketListener;
import io.github.twilight_book.utils.papi.item;
import io.github.twilight_book.utils.config.Config;
import io.github.twilight_book.utils.papi.tsbook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Book extends JavaPlugin implements Listener {
    private static Book inst;
    private static File jar;
    private static final Config config = new Config();
    private ProtocolManager protocolManager;

    @Override
    public void onEnable(){
        inst = this;
        jar = inst.getFile();
        protocolManager = ProtocolLibrary.getProtocolManager();

        load();
        reg();
    }

    @Override
    public void onDisable(){
        unload();
    }

    public void unload(){
        config.unload();
    }

    public void load() {
        config.setup(inst, jar);
    }

    public void reg() {
        new tsbook(inst, config).register();
        new   item(inst, config).register();

        DisableHeartParticle.register();
        ItemPacketListener.register();

        Bukkit.getPluginManager().registerEvents  (new EntityDamage(), inst);

        getCommand("tsbook").setExecutor    (new Commands());
        getCommand("tsbook").setTabCompleter(new CommandsTab());
    }

    public static Config getCfg(){
        return config;
    }
    public static Book getInst() { return inst; }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}