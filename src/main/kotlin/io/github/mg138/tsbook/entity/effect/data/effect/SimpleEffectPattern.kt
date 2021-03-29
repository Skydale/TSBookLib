package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.EntityStatusEffect
import io.github.mg138.tsbook.entity.effect.data.StatusEffect
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

open class SimpleEffectPattern(
    delay: (StatusEffect) -> Long,
    period: (StatusEffect) -> Long,
    condition: (LivingEntity, StatusEffect) -> Boolean,
    action: (Long, LivingEntity, StatusEffect) -> Boolean = { _, _, _ -> true },
    whenExpire: (LivingEntity, StatusEffect) -> Unit = { _, _ -> }
) {
    open val effect = label@ { effect: EntityStatusEffect ->
        val target = effect.target
        if (target !is LivingEntity) return@label

        val statusEffect = effect.statusEffect
        val ticks = statusEffect.ticks
        val delayResult = delay(statusEffect)
        val periodResult = period(statusEffect)

        val runnable = object : BukkitRunnable() {
            var i = 0L

            override fun run() {
                if (i >= ticks || condition(target, statusEffect)) { cancel(); return }

                if (!action(i, target, statusEffect)) { cancel(); return } //if the action fails (returns false), it exits
                i += periodResult
            }

            override fun cancel() {
                super.cancel()
                EffectHandler.remove(target, statusEffect.type)
                whenExpire(target, statusEffect)
            }
        }
        EffectHandler.applyRawEffect(target, statusEffect, runnable, delayResult, periodResult)
    }
}