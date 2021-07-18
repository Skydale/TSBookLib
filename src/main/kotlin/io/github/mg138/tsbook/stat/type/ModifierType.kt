package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.event.BookDamageEvent
import org.bukkit.event.EventPriority

abstract class ModifierType(
    identifier: String
) : StatType(identifier), DamageEventListener {
    abstract class ModifierTypePattern(
        identifier: String
    ) : ModifierType(identifier) {
        abstract fun condition(it: Double, event: BookDamageEvent): Boolean

        override fun onDamage(it: Double, event: BookDamageEvent) {
            if (condition(it, event)) {
                event.damagerStat.map { (type, stat) ->
                    if (type is DamageType) {
                        stat * (it / 100)
                    }
                }
            }
        }
    }

    override val damagePriority = EventPriority.LOWEST
}