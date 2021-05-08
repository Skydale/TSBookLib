package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.entity.effect.Effect
import org.bukkit.scheduler.BukkitRunnable

abstract class FlagEffect : Effect {
    override fun makeEffect(entityStatus: EntityStatus): RunningEffect {
        val runnable = object : BukkitRunnable() {
            override fun run() = this.cancel()
        }
        return RunningEffect(runnable, entityStatus.status.duration, 0L)
    }
}