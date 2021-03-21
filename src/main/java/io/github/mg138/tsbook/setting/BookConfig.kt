package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.setting.config.BookSetting
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.utils.Translate
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object BookConfig : AbstractConfig() {
    override lateinit var plugin: JavaPlugin
    override lateinit var jar: File
    override lateinit var translate: Translate
    private lateinit var bookSetting: BookSetting

    override fun load(plugin: JavaPlugin, jar: File) {
        this.plugin = plugin
        this.jar = jar

        val start = System.currentTimeMillis()

        val cb = ConfigBuilder(plugin, jar)

        plugin.logger.info("Loading configuration...")
        bookSetting = BookSetting(cb.create("", "config.yml"))

        plugin.logger.info("Loading language file: " + bookSetting.locale + "...")
        translate = Translate(cb.createFolder("lang", bookSetting.locale + ".yml"))

        plugin.logger.info("Loading item settings...")
        ItemConfig.load(cb.createMap("Items", "ID")!!, cb.createMap("Unidentified", "ID")!!)

        plugin.logger.info("Loading MythicMobs settings...")
        MobConfig.load(cb.createSectionMap("MythicMobs")!!)

        plugin.logger.info("Loading GUI settings...")
        ArmorGUIConfig.load(cb.create("GUI/", "Equipment.yml"), translate)

        plugin.logger.info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!")
    }

    override fun unload() {
        ItemConfig.unload()
        MobConfig.unload()
        ArmorGUIConfig.unload()
    }
}