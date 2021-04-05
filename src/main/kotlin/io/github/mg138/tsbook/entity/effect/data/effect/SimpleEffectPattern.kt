package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.EntityEffect
import io.github.mg138.tsbook.entity.effect.data.EntityStatus
import io.github.mg138.tsbook.entity.effect.data.Status
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

open class SimpleEffectPattern(
    delay: (Status) -> Long,
    period: (Status) -> Long,
    condition: (LivingEntity, Status) -> Boolean,
    action: (Long, LivingEntity, Status) -> Boolean = { _, _, _ -> true },
    whenExpire: (LivingEntity, Status) -> Unit = { _, _ -> }
) {
    open val effect = label@{ entityStatus: EntityStatus ->
        val target = entityStatus.target
        if (target !is LivingEntity) return@label

        val status = entityStatus.status
        val ticks = status.ticks
        val delayResult = delay(status)
        val periodResult = period(status)

        val runnable = object : BukkitRunnable() {
            var i = 0L

            override fun run() {
                if (i >= ticks || condition(target, status) || !action(i, target, status)) {
                    cancel(); return
                }
                //if time exceeded, it exits
                //if the condition isn't met, it exits
                //if the action fails (returns false), it exits
                i += periodResult
            }

            override fun cancel() {
                super.cancel()
                EffectHandler.removeFromMap(target, status.type)
                whenExpire(target, status)
            }
        }
        EffectHandler.applyEffect(EntityEffect(entityStatus, runnable), delayResult, periodResult)
    }
}