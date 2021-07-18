package io.github.mg138.tsbook.event

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import org.bukkit.plugin.java.JavaPlugin

interface PacketListener {
    fun getPacketAdapters(plugin: JavaPlugin): Iterable<PacketAdapter>

    fun register(plugin: JavaPlugin) {
        val manager = ProtocolLibrary.getProtocolManager()

        this.getPacketAdapters(plugin).forEach(manager::addPacketListener)
    }
}