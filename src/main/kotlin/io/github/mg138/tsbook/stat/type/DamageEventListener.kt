package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.event.BookDamageEvent
import org.bukkit.event.EventPriority

interface DamageEventListener {
    val damagePriority: EventPriority

    fun onDamage(it: Double, event: BookDamageEvent)
}