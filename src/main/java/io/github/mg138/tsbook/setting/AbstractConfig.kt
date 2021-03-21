package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.utils.Translate
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class AbstractConfig {
    abstract var plugin: JavaPlugin
    abstract var jar: File
    abstract var translate: Translate
    abstract fun load(plugin: JavaPlugin, jar: File)
    abstract fun unload()
}