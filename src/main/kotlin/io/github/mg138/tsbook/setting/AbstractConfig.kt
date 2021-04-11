package io.github.mg138.tsbook.setting

import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class AbstractConfig {
    abstract fun load(plugin: JavaPlugin, jar: File)
    abstract fun unload()
}