package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.event.BookDamageEvent

/**
 * Not a [DamageEventListener] because it shouldn't listen to the event,
 *
 * but instead waiting on [ChanceType] to activate it.
 */
abstract class PowerType(
    identifier: String
): StatType(identifier) {
    abstract fun onDamage(power: Double, event: BookDamageEvent)
}