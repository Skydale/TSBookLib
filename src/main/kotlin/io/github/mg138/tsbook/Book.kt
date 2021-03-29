package io.github.mg138.tsbook

import com.google.gson.Gson
import dev.reactant.reactant.core.ReactantPlugin
import io.github.mg138.tsbook.command.Armor
import io.github.mg138.tsbook.command.admin.AdminCommands
import io.github.mg138.tsbook.command.admin.AdminTabComplete
import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.item.ItemUtils
import io.github.mg138.tsbook.listener.event.click.ItemRightClick
import io.github.mg138.tsbook.listener.event.ItemUpdate
import io.github.mg138.tsbook.listener.event.click.ArmorAutoEquip
import io.github.mg138.tsbook.listener.event.damage.DamageEventHandler
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.listener.event.DisableArmorAndOffhand
import io.github.mg138.tsbook.listener.event.inventory.EquipmentGUIHandler
import io.github.mg138.tsbook.listener.packet.DisableHeartParticle
import io.github.mg138.tsbook.listener.packet.ItemPacket
import io.github.mg138.tsbook.listener.event.damage.HealthIndicator
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.util.papi.PlaceholderExpansionTSBook
import me.pikamug.localelib.LocaleLib
import me.pikamug.localelib.LocaleManager
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@ReactantPlugin([
    "io.github.mg138.tsbook"
])
class Book : JavaPlugin() {
    companion object {
        lateinit var inst: Book
        lateinit var jar: File

        lateinit var equipmentGUIHandler: EquipmentGUIHandler

        var gson = Gson()
        var localeManager: LocaleManager? = null
    }

    override fun onEnable() {
        inst = this
        jar = inst.file
        preReg()
        load()
        reg()
    }

    override fun onDisable() {
        unload()
    }

    fun unload() {
        BookConfig.unload()
        ItemUtils.unload()
        EffectHandler.unload()
        DamageEventHandler.unload()
        ItemRightClick.unload()
        HealthIndicator.unload()
        DamageIndicator.unload()
        equipmentGUIHandler.unload()
    }

    fun load() {
        BookConfig.load(inst, jar)
    }

    private fun preReg() {
        if (server.pluginManager.isPluginEnabled("LocaleLib")) {
            localeManager = (server.pluginManager.getPlugin("LocaleLib") as LocaleLib).localeManager
        }
        PlaceholderExpansionTSBook(inst).register()
    }

    private fun reg() {
        server.worlds.forEach { it.setGameRule(GameRule.KEEP_INVENTORY, true) }
        DisableHeartParticle.register()
        ItemPacket.register()

        equipmentGUIHandler = EquipmentGUIHandler()
        Bukkit.getPluginManager().registerEvents(DamageEventHandler(), inst)
        Bukkit.getPluginManager().registerEvents(ItemUpdate(), inst)
        Bukkit.getPluginManager().registerEvents(ItemRightClick(), inst)
        Bukkit.getPluginManager().registerEvents(HealthIndicator(), inst)
        Bukkit.getPluginManager().registerEvents(equipmentGUIHandler, inst)
        Bukkit.getPluginManager().registerEvents(ArmorAutoEquip(), inst)
        Bukkit.getPluginManager().registerEvents(DisableArmorAndOffhand(), inst)

        getCommand("tsbook")!!.setExecutor(AdminCommands())
        getCommand("tsbook")!!.tabCompleter = AdminTabComplete()
        getCommand("armor")!!.setExecutor(Armor())
    }
}