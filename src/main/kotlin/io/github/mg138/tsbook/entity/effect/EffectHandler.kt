package io.github.mg138.tsbook.entity.effect

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.entity.effect.data.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Entity
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.HashMap

object EffectHandler {
    private val activeEffects: MutableMap<LivingEntity, MutableMap<StatusType, EntityEffect>> = HashMap()

    fun applyEffect(entityEffect: EntityEffect, delay: Long, period: Long) {
        val entityStatus = entityEffect.entityStatus
        val target = entityStatus.target
        if (target !is LivingEntity) return

        val type = entityStatus.status.type
        activeEffects.putIfAbsent(target, EnumMap<StatusType, EntityEffect>(StatusType::class.java))

        activeEffects[target]!![type]?.runnable?.cancel()
        activeEffects[target]!![type] = entityEffect
        entityEffect.runnable.runTaskTimer(Book.inst, delay, period)
    }

    fun apply(type: StatusType, target: LivingEntity, power: Double, time: Long) {
        RegisteredEffects.effects[type]?.invoke(EntityStatus(target, type, power, time))
    }

    fun removeFromMap(entity: LivingEntity, type: StatusType) {
        activeEffects[entity]?.remove(type)
    }

    fun remove(entity: LivingEntity, type: StatusType) {
        try {
            activeEffects[entity]!![type]!!.runnable.cancel()
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("The entity doesn't have any active effects")
        }
    }

    fun removeAll(entity: LivingEntity) {
        val removing = try {
            activeEffects[entity]!!
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("The entity doesn't have any active effects")
        }
        removing.forEach { (_, entityEffect) -> entityEffect.runnable.cancel() }
        removing.clear()
        activeEffects.remove(entity)
    }

    fun unload() {
        val removing = HashMap(activeEffects)
        removing.forEach { (_, map) -> map.forEach { (_, entityEffect) -> entityEffect.runnable.cancel() }}
        removing.clear()
        activeEffects.clear()
    }
}
