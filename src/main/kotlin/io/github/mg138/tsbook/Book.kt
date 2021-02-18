package io.github.mg138.tsbook

import com.google.gson.Gson
import dev.reactant.reactant.core.ReactantPlugin
import io.github.mg138.tsbook.command.Armor
import io.github.mg138.tsbook.command.admin.AdminCommands
import io.github.mg138.tsbook.command.admin.AdminTabComplete
import io.github.mg138.tsbook.entities.effect.EffectHandler
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.listener.event.ItemRightClick
import io.github.mg138.tsbook.listener.event.ItemUpdate
import io.github.mg138.tsbook.listener.event.click.RightClickEvent
import io.github.mg138.tsbook.listener.event.damage.DamageEventHandler
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.listener.event.inventory.EquipmentGUIHandler
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
        lateinit var equipmentGUIHandler: EquipmentGUIHandler
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
        DamageEventHandler.unload()
        ItemRightClick.unload()
        HealthIndicator.unload()
        DamageIndicator.unload()
        equipmentGUIHandler.unload()
    }

    fun load() {
        cfg.load(inst, jar)
    }

    fun reg() {
        tsbook(inst, cfg).register()
        unid(inst, cfg).register()
        DisableHeartParticle.register()
        ItemPacket.register()

        Bukkit.getPluginManager().registerEvents(DamageEventHandler(), inst)
        Bukkit.getPluginManager().registerEvents(ItemUpdate(), inst)
        Bukkit.getPluginManager().registerEvents(ItemRightClick(), inst)
        Bukkit.getPluginManager().registerEvents(HealthIndicator(), inst)
        equipmentGUIHandler = EquipmentGUIHandler(cfg)
        Bukkit.getPluginManager().registerEvents(equipmentGUIHandler, inst)
        Bukkit.getPluginManager().registerEvents(RightClickEvent(cfg), inst)

        getCommand("tsbook")!!.setExecutor(AdminCommands())
        getCommand("tsbook")!!.tabCompleter = AdminTabComplete()
        getCommand("armor")!!.setExecutor(Armor())
    }
}