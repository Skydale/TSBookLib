package io.github.mg138.tsbook;

import io.github.mg138.tsbook.command.CommandsTab;
import java.io.File;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.listener.event.ItemRightClick;
import io.github.mg138.tsbook.listener.event.ItemUpdate;
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator;
import io.github.mg138.tsbook.players.HealthIndicator;
import io.github.mg138.tsbook.listener.packet.DisableHeartParticle;
import java.util.Objects;

import com.google.gson.Gson;
import dev.reactant.reactant.core.ReactantPlugin;

import io.github.mg138.tsbook.listener.event.damage.EntityDamage;
import io.github.mg138.tsbook.command.Commands;
import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.listener.packet.ItemPacket;
import io.github.mg138.tsbook.utils.config.Config;
import io.github.mg138.tsbook.utils.papi.tsbook;
import io.github.mg138.tsbook.utils.papi.unid;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@ReactantPlugin(servicePackages = "io.github.mg138.tsbook")

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
        EntityDamage.unload();
        ItemRightClick.unload();
        HealthIndicator.unload();
        DamageIndicator.unload();
    }

    public void load() {
        config.setup(inst, jar);
    }

    public void reg() {
        new tsbook(inst, config).register();
        new unid(inst, config).register();

        DisableHeartParticle.register();
        ItemPacket.register();

        Bukkit.getPluginManager().registerEvents(new EntityDamage(), inst);
        Bukkit.getPluginManager().registerEvents(new ItemUpdate(), inst);
        Bukkit.getPluginManager().registerEvents(new ItemRightClick(), inst);
        Bukkit.getPluginManager().registerEvents(new HealthIndicator(), inst);

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