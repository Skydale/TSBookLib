package io.github.mg138.tsbook.listener.event.damage.utils

import io.github.mg138.tsbook.stat.StatType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class CustomDamageEvent(val entity: LivingEntity, val damager: LivingEntity, private val damages: MutableMap<StatType, Double> = EnumMap(StatType::class.java)) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    fun getDamages(): Map<StatType, Double> {
        return damages
    }

    fun addDamage(type: StatType, damage: Double) {
        damages[type] = damage
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}