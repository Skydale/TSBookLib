package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.entity.effect.pattern.DamagingEffect
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity

/*
@Component
class Bleeding : DamagingEffect(
    StatType.DAMAGE_BLEED,
    7L
) {
    override val id = "BLEEDING"

    override val visualEffect: (LivingEntity) -> Unit = {
        it.world.spawnParticle(
            Particle.BLOCK_DUST,
            it.location,
            20,
            Bukkit.createBlockData(Material.REDSTONE_BLOCK)
        )
    }
}
 */