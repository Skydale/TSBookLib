package io.github.mg138.tsbook.entities.effect

import io.github.mg138.tsbook.Book
import org.bukkit.entity.LivingEntity
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import org.bukkit.scheduler.BukkitRunnable
import io.github.mg138.tsbook.entities.effect.data.StatusEffect
import io.github.mg138.tsbook.entities.effect.data.map.RegisteredEffects
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect
import org.bukkit.entity.Entity
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.HashMap

object EffectHandler {
    private val activeRunnable: MutableMap<LivingEntity, MutableMap<StatusEffectType, BukkitRunnable>> = HashMap()
    private val activeEffect: MutableMap<LivingEntity, MutableMap<StatusEffectType, StatusEffect>> = HashMap()

    fun applyRawEffect(target: Entity, effect: StatusEffect, runnable: BukkitRunnable, delay: Long, period: Long) {
        if (target !is LivingEntity) return
        addRunnable(target, effect, runnable)
        addEffect(target, effect)
        runnable.runTaskTimer(Book.inst, delay, period)
    }

    fun apply(type: StatusEffectType, target: Entity, power: Double, time: Long) {
        if (target !is LivingEntity) return
        activeRunnable[target]?.get(type)?.cancel()
        activeEffect[target]?.remove(type)

        RegisteredEffects.Effects[type]?.invoke(EntityStatusEffect(target, type, power, time))
    }

    fun unload() {
        val removing: MutableSet<BukkitRunnable> = HashSet()
        activeRunnable.forEach { (_, runnableMap) ->
            runnableMap.forEach { (_, runnable) ->
                removing.add(runnable)
            }
        }
        removing.forEach { it.cancel() }
        removing.clear()
        activeRunnable.clear()
        activeEffect.clear()
    }

    fun getEffect(entity: LivingEntity, type: StatusEffectType): StatusEffect? {
        val effects = activeEffect[entity] ?: return null
        return effects[type]
    }

    fun hasEffect(entity: LivingEntity, type: StatusEffectType): Boolean {
        val effects = activeEffect[entity] ?: return false
        return effects.containsKey(type)
    }

    private fun addEffect(entity: LivingEntity, effect: StatusEffect) {
        activeEffect.putIfAbsent(entity, EnumMap(StatusEffectType::class.java))
        activeEffect[entity]?.set(effect.type, effect)
    }

    private fun addRunnable(entity: LivingEntity, effect: StatusEffect, runnable: BukkitRunnable) {
        val runnables = activeRunnable.getOrDefault(entity, EnumMap(StatusEffectType::class.java))
        runnables.put(effect.type, runnable)?.cancel()
        activeRunnable[entity] = runnables
    }

    fun remove(entity: LivingEntity, type: StatusEffectType) {
        activeEffect[entity]?.remove(type)
        activeRunnable[entity]?.get(type)?.cancel()
    }

    fun remove(entity: LivingEntity) {
        val removing = try {
            activeRunnable[entity]!!
        } catch (e: NullPointerException) {
            throw NullPointerException("The entity doesn't have any active effects")
        }
        removing.forEach { (_, runnable) -> runnable.cancel() }
        removing.clear()
        activeRunnable.remove(entity)
        activeEffect.remove(entity)
    }
}