package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import org.bukkit.scheduler.BukkitRunnable

abstract class ActiveEffect(
    val effect: Effect,
    val property: EffectProperty,
    val period: Long,
    private val delay: Long,
    private val effectManager: EffectManager
) {
    private val runnable = object : BukkitRunnable() {
        override fun run() {
            tick()
        }

        override fun cancel() {
            deactivate()
            effectManager.remove(property.target, effectType)
        }
    }

    val effectType = effect.getType()

    abstract fun tick()

    open fun deactivate() {}

    fun activate() = runnable.runTaskTimer(Book.inst, delay, period)

    fun cancelAndRemove() = runnable.cancel()
}