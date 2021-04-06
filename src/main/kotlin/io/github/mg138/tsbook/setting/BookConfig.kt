package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.setting.config.BookSetting
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.setting.stat.StatDisplay
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.util.LanguageFile
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object BookConfig : AbstractConfig() {
    object language {
        object healthIndicator {
            lateinit var title: String
        }

        fun load(languageFile: LanguageFile) {
            healthIndicator.title = languageFile.get("health_indicator.title")
        }
    }

    override lateinit var languageFile: LanguageFile
    private lateinit var bookSetting: BookSetting

    override fun load(plugin: JavaPlugin, jar: File) {
        val start = System.currentTimeMillis()

        val cb = ConfigBuilder(plugin, jar)

        plugin.logger.info("Loading configuration...")
        bookSetting = BookSetting(cb.create("", "config.yml"))

        plugin.logger.info("Loading language file: ${bookSetting.locale}...")
        languageFile = LanguageFile(cb.createFolder("lang", "${bookSetting.locale}.yml"))
        language.load(languageFile)

        plugin.logger.info("Caching stat format/name...")
        StatDisplay.load(languageFile)

        plugin.logger.info("Loading item settings...")
        ItemConfig.load(cb.createMap("Items", "ID"), cb.createMap("Unidentified", "ID"))

        plugin.logger.info("Loading MythicMobs settings...")
        MobConfig.load(cb.createSectionMap("MythicMobs"))

        plugin.logger.info("Loading GUI settings...")
        ArmorGUIConfig.load(cb.create("GUI", "Equipment.yml"))

        plugin.logger.info("Took me... [" + (System.currentTimeMillis() - start) + "ms] to load!")
    }

    override fun unload() {
        ItemConfig.unload()
        MobConfig.unload()
        ArmorGUIConfig.unload()
    }
}