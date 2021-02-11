package io.github.mg138.tsbook.listener.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import io.github.mg138.tsbook.Book;

import org.bukkit.Particle;

public class DisableHeartParticle {
    public static void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Book.inst, ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getNewParticles().getValues().get(0).getParticle() == Particle.DAMAGE_INDICATOR) {
                    event.setCancelled(true);
                }
            }
        });
    }
}