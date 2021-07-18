package io.github.mg138.tsbook.entity.effect.event

import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.type.DamageModifier
import io.github.mg138.tsbook.event.EventListener
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.plugin.java.JavaPlugin

object EffectEventHandler : EventListener {
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity as? LivingEntity ?: return

        if (EffectManager.hasNone(entity)) return

        EffectManager.filterEffects<DamageModifier>(entity).forEach {
            it.onDamage(event)
        }
    }
}