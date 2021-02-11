package io.github.mg138.tsbook

import com.google.gson.Gson
import dev.reactant.reactant.core.ReactantPlugin
import io.github.mg138.tsbook.command.Commands
import io.github.mg138.tsbook.command.CommandsTab
import io.github.mg138.tsbook.entities.effect.EffectHandler
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.listener.event.ItemRightClick
import io.github.mg138.tsbook.listener.event.ItemUpdate
import io.github.mg138.tsbook.listener.event.damage.EntityDamage
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.listener.packet.DisableHeartParticle
import io.github.mg138.tsbook.listener.packet.ItemPacket
import io.github.mg138.tsbook.players.util.HealthIndicator
import io.github.mg138.tsbook.utils.config.Config
import io.github.mg138.tsbook.utils.papi.tsbook
import io.github.mg138.tsbook.utils.papi.unid
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@ReactantPlugin([
    "io.github.mg138.tsbook"
])
class Book : JavaPlugin() {
    companion object {
        lateinit var inst: Book
        lateinit var jar: File
        var gson = Gson()
        val cfg: Config = Config.getInstance()
    }

    override fun onEnable() {
        inst = this
        jar = inst.file
        load()
        reg()
    }

    override fun onDisable() {
        unload()
    }

    fun unload() {
        cfg.unload()
        ItemUtils.unload()
        EffectHandler.unload()
        EntityDamage.unload()
        ItemRightClick.unload()
        HealthIndicator.unload()
        DamageIndicator.unload()
    }

    fun load() {
        cfg.load(inst, jar)
    }

    fun reg() {
        tsbook(inst, cfg).register()
        unid(inst, cfg).register()
        DisableHeartParticle.register()
        ItemPacket.register()
        Bukkit.getPluginManager().registerEvents(EntityDamage(), inst)
        Bukkit.getPluginManager().registerEvents(ItemUpdate(), inst)
        Bukkit.getPluginManager().registerEvents(ItemRightClick(), inst)
        Bukkit.getPluginManager().registerEvents(HealthIndicator(), inst)
        getCommand("tsbook")!!.setExecutor(Commands())
        getCommand("tsbook")!!.tabCompleter = CommandsTab()
    }
}