package io.github.mg138.tsbook.listener.packet

import com.comphenix.packetwrapper.WrapperPlayServerWorldParticles
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import io.github.mg138.service.reactant.service.PacketService
import org.bukkit.Particle

// todo rewrite
/*
@Component
class DisableHeartParticle(
    private val packetService: PacketService
) : LifeCycleHook {
    override fun onEnable() {
        packetService.packetAdapterService {
            WrapperPlayServerWorldParticles.TYPE.onSending { event ->
                val particle = WrapperPlayServerWorldParticles(event.packet).particle.particle

                if (particle == Particle.DAMAGE_INDICATOR) {
                    event.isCancelled = true
                }
            }
        }
    }
}
 */