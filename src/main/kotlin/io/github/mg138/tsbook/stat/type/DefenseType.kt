package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.event.BookDamageEvent
import io.github.mg138.tsbook.stat.util.StatUtil
import org.bukkit.event.EventPriority

abstract class DefenseType(
    identifier: String
) : StatType(identifier), DamageEventListener {
    abstract class DefenseTypePattern(
        identifier: String,
        val damageType: DamageType
    ): DefenseType(identifier) {
        override fun onDamage(it: Double, event: BookDamageEvent) {
            event.damagerStat.computeIfPresent(damageType) { damage ->
                StatUtil.defense(damage, it)
            }
        }
    }

    override val damagePriority = EventPriority.LOWEST
}