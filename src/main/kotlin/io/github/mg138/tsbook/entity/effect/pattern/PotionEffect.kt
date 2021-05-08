package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.entity.effect.status.Status
import io.github.mg138.tsbook.entity.effect.Effect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

abstract class PotionEffect(
    private val type: PotionEffectType,
    private val duration: (Status) -> Int = { 2147483647 },
    private val amplifier: (Status) -> Int = { 0 },
    private val ambient: Boolean = false,
    private val particles: Boolean = false,
    private val icon: Boolean = true
) : Effect {
    override fun makeEffect(entityStatus: EntityStatus): RunningEffect {
        val target = entityStatus.target
        val status = entityStatus.status

        target.addPotionEffect(
            org.bukkit.potion.PotionEffect(type, duration(status), amplifier(status), ambient, particles, icon)
        )

        val runnable = object : BukkitRunnable() {
            override fun run() = this.cancel()

            override fun cancel() {
                super.cancel()
                target.removePotionEffect(type)
            }
        }
        return RunningEffect(runnable, status.duration, 0L)
    }
}