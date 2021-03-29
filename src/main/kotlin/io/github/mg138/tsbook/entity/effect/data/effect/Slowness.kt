package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.EntityStatusEffect
import io.github.mg138.tsbook.stat.util.StatUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

object Slowness {
    val effect = label@ { effect: EntityStatusEffect ->
        val target = effect.target
        if (target !is LivingEntity) return@label

        val statusEffect = effect.statusEffect
        val delayResult = statusEffect.ticks
        val periodResult = 0L

        val attribute = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!
        val old = attribute.baseValue
        attribute.baseValue = StatUtil.calculateModifier(old, -1 * statusEffect.power)

        val runnable = object : BukkitRunnable() {
            override fun run() {
                cancel(); return
            }

            override fun cancel() {
                super.cancel()
                EffectHandler.remove(target, statusEffect.type)
                attribute.baseValue = old
            }
        }
        EffectHandler.applyRawEffect(target, statusEffect, runnable, delayResult, periodResult)
    }
}