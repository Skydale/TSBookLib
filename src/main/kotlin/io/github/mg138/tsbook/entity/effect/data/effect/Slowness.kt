package io.github.mg138.tsbook.entity.effect.data.effect

import io.github.mg138.tsbook.entity.effect.EffectHandler
import io.github.mg138.tsbook.entity.effect.data.EntityEffect
import io.github.mg138.tsbook.entity.effect.data.EntityStatus
import io.github.mg138.tsbook.item.attribute.stat.util.StatUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

object Slowness {
    val effect = label@ { entityStatus: EntityStatus ->
        val target = entityStatus.target
        if (target !is LivingEntity) return@label

        val status = entityStatus.status
        val delayResult = status.ticks
        val periodResult = 0L

        val attribute = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!
        val old = attribute.baseValue
        attribute.baseValue = StatUtil.calculateModifier(old, -1 * status.power)

        val runnable = object : BukkitRunnable() {
            override fun run() {
                cancel(); return
            }

            override fun cancel() {
                super.cancel()
                EffectHandler.removeFromMap(target, status.type)
                attribute.baseValue = old
            }
        }
        EffectHandler.applyEffect(EntityEffect(entityStatus, runnable), delayResult, periodResult)
    }
}