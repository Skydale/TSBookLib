package io.github.twilight_book.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.twilight_book.Book;
import org.bukkit.Particle;

public class DisableHeartParticle {
    public static void register() {
        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                    if (event.getPacket().getNewParticles().getValues().get(0).getParticle() == Particle.DAMAGE_INDICATOR) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}