package io.github.mg138.tsbook

import com.google.gson.Gson
import dev.reactant.reactant.core.ReactantPlugin
import io.github.mg138.tsbook.command.Armor
import io.github.mg138.tsbook.command.admin.AdminCommands
import io.github.mg138.tsbook.command.admin.AdminTabComplete
import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.item.util.ItemUtil
import io.github.mg138.tsbook.listener.event.DisableArmorAndOffhand
import io.github.mg138.tsbook.listener.event.ItemUpdate
import io.github.mg138.tsbook.listener.event.click.ArmorAutoEquip
import io.github.mg138.tsbook.listener.event.click.ItemRightClick
import io.github.mg138.tsbook.listener.event.damage.DamageEventHandler
import io.github.mg138.tsbook.listener.event.damage.HealthIndicator
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator
import io.github.mg138.tsbook.listener.event.inventory.EquipmentGUIHandler
import io.github.mg138.tsbook.listener.packet.DisableHeartParticle
import io.github.mg138.tsbook.listener.packet.ItemPacket
import io.github.mg138.tsbook.setting.BookConfig
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

        var gson = Gson()
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
        ItemUtil.unload()
        EffectHandler.unload()
        DamageEventHandler.unload()
        ItemRightClick.unload()
        HealthIndicator.unload()
        DamageIndicator.unload()
        EquipmentGUIHandler.unload()
    }

    fun load() {
        BookConfig.load(inst, jar)
    }

    private fun preReg() {
    }

    private fun reg() {
        server.worlds.forEach { it.setGameRule(GameRule.KEEP_INVENTORY, true) }
        DisableHeartParticle.register()
        ItemPacket.register()

        Bukkit.getPluginManager().registerEvents(DamageEventHandler, inst)
        Bukkit.getPluginManager().registerEvents(ItemUpdate, inst)
        Bukkit.getPluginManager().registerEvents(ItemRightClick, inst)
        Bukkit.getPluginManager().registerEvents(HealthIndicator, inst)
        Bukkit.getPluginManager().registerEvents(EquipmentGUIHandler, inst)
        Bukkit.getPluginManager().registerEvents(ArmorAutoEquip, inst)
        Bukkit.getPluginManager().registerEvents(DisableArmorAndOffhand, inst)

        val tsbook = getCommand("tsbook")!!
        tsbook.setExecutor(AdminCommands)
        tsbook.tabCompleter = AdminTabComplete
        getCommand("armor")!!.setExecutor(Armor)
    }
}