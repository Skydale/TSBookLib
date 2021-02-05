package io.github.mg138.tsbook;

import io.github.mg138.tsbook.command.CommandsTab;
import java.io.File;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.listener.event.ItemRightClick;
import io.github.mg138.tsbook.listener.event.ItemUpdate;
import io.github.mg138.tsbook.listener.event.utils.EnemyHealthIndicator;
import io.github.mg138.tsbook.listener.packet.DisableHeartParticle;
import java.util.Objects;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import dev.reactant.reactant.core.ReactantPlugin;

import io.github.mg138.tsbook.listener.event.EntityDamage;
import io.github.mg138.tsbook.command.Commands;
import io.github.mg138.tsbook.entities.EffectHandler;
import io.github.mg138.tsbook.listener.event.utils.DamageIndicator;
import io.github.mg138.tsbook.listener.packet.ItemPacket;
import io.github.mg138.tsbook.utils.papi.item;
import io.github.mg138.tsbook.utils.config.Config;
import io.github.mg138.tsbook.utils.papi.tsbook;
import io.github.mg138.tsbook.utils.papi.unid;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@ReactantPlugin(servicePackages = {
        "io.github.mg138.tsbook"
})

public class Book extends JavaPlugin {
    public static final Gson gson = new Gson();
    private static Book inst;
    private static File jar;
    private static final Config config = new Config();

    @Override
    public void onEnable() {
        inst = this;
        jar = inst.getFile();

        load();
        reg();
    }

    @Override
    public void onDisable() {
        unload();

    }

    public void unload() {
        config.unload();
        ItemUtils.unload();
        EffectHandler.unload();
        DamageIndicator.unload();
        EntityDamage.unload();
        ItemRightClick.unload();
        EnemyHealthIndicator.unload();
    }

    public void load() {
        config.setup(inst, jar);
    }

    public void reg() {
        new tsbook(inst, config).register();
        new item(inst, config).register();
        new unid(inst, config).register();

        DisableHeartParticle.register();
        ItemPacket.register();

        Bukkit.getPluginManager().registerEvents(new EntityDamage(), inst);
        Bukkit.getPluginManager().registerEvents(new ItemUpdate(), inst);
        Bukkit.getPluginManager().registerEvents(new ItemRightClick(), inst);
        Bukkit.getPluginManager().registerEvents(new EnemyHealthIndicator(), inst);

        Objects.requireNonNull(getCommand("tsbook")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("tsbook")).setTabCompleter(new CommandsTab());
    }

    public static Config getCfg() {
        return config;
    }

    public static Book getInst() {
        return inst;
    }
}