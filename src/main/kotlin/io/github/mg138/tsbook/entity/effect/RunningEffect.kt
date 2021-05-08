package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.Book
import org.bukkit.scheduler.BukkitRunnable

class RunningEffect(val runnable: BukkitRunnable, val delay: Long, val period: Long) {
    fun runTaskTimer() = runnable.runTaskTimer(Book.inst, delay, period)
}