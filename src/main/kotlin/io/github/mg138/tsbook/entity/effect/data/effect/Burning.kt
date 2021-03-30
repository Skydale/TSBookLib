package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.Particle

object Burning : SimpleEffectPattern(
    delay = { 0 },
    period = { 10 },
    condition = { target, status ->
        !DamageHandler.simpleDamage(
            target,
            status.power,
            StatType.DAMAGE_IGNIS,
            true
        )
    },
    action = { _, target, _ ->
        target.world.spawnParticle(Particle.LAVA, target.location, 6)
        true
    }
)