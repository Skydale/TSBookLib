package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.DamagingEffect
import io.github.mg138.tsbook.entity.effect.EffectType
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle

@Component
class Bleeding : DamagingEffect(
    StatType.DAMAGE_BLEED,
    7L,
    visualEffect = {
        it.world.spawnParticle(
            Particle.BLOCK_DUST,
            it.location,
            20,
            Bukkit.createBlockData(Material.REDSTONE_BLOCK)
        )
    }
) {
    override fun getType() = EffectType.PresetTypes.bleeding
}