package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.event.BookDamageEvent
import org.bukkit.event.EventPriority

abstract class DamageType(identifier: String) : StatType(identifier), DamageEventListener {
    override val damagePriority = EventPriority.HIGHEST

    override fun onDamage(it: Double, event: BookDamageEvent) {
        event.damageEvent.damage += it
    }
}