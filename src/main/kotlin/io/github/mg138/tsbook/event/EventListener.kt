package io.github.mg138.tsbook.event

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

interface EventListener : Listener {
    fun register(plugin: JavaPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}