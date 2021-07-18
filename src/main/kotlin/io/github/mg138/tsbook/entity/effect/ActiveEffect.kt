package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
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
            effectManager.remove(property.target, id)
        }
    }

    val id = effect.id

    abstract fun tick()

    open fun deactivate() {}
    fun activate() = runnable.runTaskTimer(Book.inst, delay, period)
    fun stop() = runnable.cancel()
}