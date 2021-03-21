package io.github.mg138.tsbook.listener.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.github.mg138.tsbook.Book
import org.bukkit.Particle

object DisableHeartParticle {
    fun register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(Book.inst, ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
                override fun onPacketSending(event: PacketEvent) {
                    if (event.packet.newParticles.values[0].particle == Particle.DAMAGE_INDICATOR) {
                        event.isCancelled = true
                    }
                }
            }
        )
    }
}