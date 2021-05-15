package io.github.mg138.tsbook.entity.effect.listener

import org.bukkit.event.entity.EntityDamageEvent

interface DamageModifier {
    fun onDamage(event: EntityDamageEvent)
}