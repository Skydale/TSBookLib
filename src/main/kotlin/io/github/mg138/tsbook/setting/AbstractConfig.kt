package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.util.LanguageFile
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class AbstractConfig {
    abstract var languageFile: LanguageFile
    abstract fun load(plugin: JavaPlugin, jar: File)
    abstract fun unload()
}