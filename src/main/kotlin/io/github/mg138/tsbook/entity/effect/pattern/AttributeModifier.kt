package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.entity.effect.status.Status
import io.github.mg138.tsbook.entity.effect.Effect
import org.bukkit.attribute.Attribute
import org.bukkit.scheduler.BukkitRunnable

abstract class AttributeModifier(
    private val attribute: Attribute,
    private val modified: (Double, Status) -> Double
) : Effect {
    override fun makeEffect(entityStatus: EntityStatus): RunningEffect {
        val status = entityStatus.status

        val attribute = entityStatus.target.getAttribute(attribute)!!

        val old = attribute.baseValue
        attribute.baseValue = modified(old, status)

        val runnable = object : BukkitRunnable() {
            override fun run() = this.cancel()

            override fun cancel() {
                super.cancel()
                attribute.baseValue = old
            }
        }
        return RunningEffect(runnable, status.duration, 0L)
    }
}