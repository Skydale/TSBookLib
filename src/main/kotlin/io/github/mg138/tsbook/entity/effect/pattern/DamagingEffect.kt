package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

abstract class DamagingEffect(
    private val type: StatType,
    private val period: Long,
    private val effect: (LivingEntity) -> Unit
) : Effect {
    override fun makeEffect(entityStatus: EntityStatus): RunningEffect {
        val target = entityStatus.target
        val status = entityStatus.status

        val duration = status.duration
        val power = status.power

        val runnable = object : BukkitRunnable() {
            var i = 0L

            override fun run() {
                if (i >= duration || !DamageHandler.simpleDamage(target, power, type)) {
                    this.cancel(); return
                }

                effect(target)

                i += period
            }
        }
        return RunningEffect(runnable, 0L, period)
    }
}