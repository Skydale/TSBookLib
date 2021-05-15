package io.github.mg138.tsbook.entity.effect.util

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.entity.effect.*
import io.github.mg138.tsbook.entity.effect.bridge.EffectHandler
import io.github.mg138.tsbook.entity.effect.listener.DamageModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

@Component
class EffectManager(
    private val handler: EffectHandler,
    private val eventService: EventService
) : LifeCycleHook {
    private val effects = EntityEffects()

    private fun makeEffect(type: EffectType, property: EffectProperty) = handler.makeEffect(type, property, this)

    // Note: this will activate the effect.
    fun apply(effect: ActiveEffect) = effects.apply(effect)

    fun apply(type: EffectType, property: EffectProperty) = this.apply(makeEffect(type, property))

    operator fun plusAssign(effect: ActiveEffect) {
        this.apply(effect)
    }

    operator fun get(entity: LivingEntity) = effects[entity]

    operator fun get(entity: LivingEntity, type: EffectType) = effects[entity, type]

    inline fun <reified T : Any> LivingEntity.filterEffects(operation: (T) -> Unit) =
        get(this).effects.forEach { if (it is T) operation(it) }

    fun Entity.hasEffect(type: EffectType) = effects.has(this, type)

    fun Entity.hasEffects() = effects.hasEffects(this)

    fun remove(entity: LivingEntity, type: EffectType) = effects.remove(entity, type)

    fun removeAll(entity: LivingEntity) = effects.removeAll(entity)

    override fun onEnable() {
        eventService {
            EntityDamageEvent::class.observable()
                .filter { it.entity.hasEffects() }
                .subscribe { event ->
                    val entity = event.entity
                    if (entity !is LivingEntity) return@subscribe

                    entity.filterEffects<DamageModifier> {
                        it.onDamage(event)
                    }
                }
        }
    }

    override fun onDisable() {
        effects.clear()
    }
}