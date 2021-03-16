package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.setting.config.BookConfig
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.utils.Translate
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BookSetting : AbstractSetting() {

    var bookConfig: BookConfig = null
    override fun load(plugin: JavaPlugin, jar: File) {
        this.plugin = plugin
        this.jar = jar
        val start = System.currentTimeMillis()
        val cb = ConfigBuilder(plugin, jar)
        plugin.logger.info("Loading configuration...")
        bookConfig = BookConfig(cb.create("", "config.yml"))
        plugin.logger.info("Loading language file: " + bookConfig!!.locale + "...")
        translate = Translate(cb.createFolder("lang", bookConfig!!.locale + ".yml"))
        plugin.logger.info("Loading item settings...")
        itemConfig.load(cb.createMap("Items", "ID")!!, cb.createMap("Unidentified", "ID")!!)
        plugin.logger.info("Loading MythicMobs settings...")
        mmMobs = MobConfig(cb.createSectionMap("MythicMobs"))
        plugin.logger.info("Loading GUI settings...")
        armorGUIConfig.load(cb.create("GUI/", "Equipment.yml"), translate)
        plugin.logger.info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!")
    }

    override fun unload() {
        itemConfig.unload()
        mmMobs!!.clear()
    }

    companion object {
        val instance = BookSetting()
    }
}