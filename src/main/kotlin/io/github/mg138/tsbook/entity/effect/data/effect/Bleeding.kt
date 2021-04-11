package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.attribute.stat.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle

object Bleeding : SimpleEffectPattern(
    delay = { 0 },
    period = { 7 },
    condition = { target, status ->
        !DamageHandler.simpleDamage(
            target,
            status.power,
            StatType.DAMAGE_BLEED,
            true
        )
    },
    action = { _, target, _ ->
        target.world.spawnParticle(
            Particle.BLOCK_DUST,
            target.location,
            20,
            Bukkit.createBlockData(Material.REDSTONE_BLOCK)
        )
        true
    }
)