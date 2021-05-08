package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.DamagingEffect
import io.github.mg138.tsbook.entity.effect.status.StatusType
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import org.bukkit.Particle

@Component
class Burning : DamagingEffect(
    StatType.DAMAGE_IGNIS,
    10L,
    {
        it.world.spawnParticle(Particle.LAVA, it.location, 6)
    }
) {
    override fun getType() = StatusType.PresetTypes.burning
}