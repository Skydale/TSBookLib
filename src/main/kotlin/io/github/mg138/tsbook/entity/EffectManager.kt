package io.github.mg138.tsbook.entity

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.core.dependency.injection.components.Components
import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.EntityEffect
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.status.StatusType
import org.bukkit.entity.LivingEntity
import java.util.*

@Component
class EffectManager(
    effects: Components<Effect>
) : LifeCycleHook {
    private val registeredEffects: Map<StatusType, Effect> = effects.associateBy { it.getType() }
    private val activeEffects: MutableMap<LivingEntity, MutableMap<StatusType, EntityEffect>> = HashMap()

    fun getEffects(entity: LivingEntity) = activeEffects[entity]
    fun getEffect(entity: LivingEntity, type: StatusType) = this.getEffects(entity)?.get(type)

    fun applyEffect(entityEffect: EntityEffect) {
        val entityStatus = entityEffect.entityStatus
        val target = entityStatus.target
        val type = entityStatus.status.type

        this.remove(target, type)
        activeEffects.putIfAbsent(target, HashMap())
        activeEffects[target]!![type] = entityEffect
        entityEffect.effect.runTaskTimer()
    }

    fun apply(entity: LivingEntity, type: StatusType, power: Double, time: Long) {
        val entityStatus = EntityStatus(entity, type, power, time)
        registeredEffects[type]?.makeEffect(entityStatus)?.let {
            this.applyEffect(EntityEffect(entityStatus, it))
        }
    }

    fun remove(entity: LivingEntity, type: StatusType) {
        activeEffects[entity]?.let {
            it[type]?.effect?.runnable?.cancel()
            it.remove(type)
        }
    }

    fun removeAll(entity: LivingEntity) {
        activeEffects[entity]?.values?.forEach {
            it.effect.runnable.cancel()
        }
        activeEffects.remove(entity)
    }

    override fun onDisable() {
        activeEffects.values.forEach { map ->
            map.values.forEach {
                it.effect.runnable.cancel()
            }
        }
        activeEffects.clear()
    }
}