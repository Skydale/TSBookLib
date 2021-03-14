package io.github.mg138.tsbook.listener.event.damage.utils

import io.github.mg138.tsbook.items.data.stat.StatType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class CustomDamageEvent(var entity: LivingEntity, var damager: LivingEntity) : Event() {
    private var damages: MutableMap<StatType, Double> = EnumMap(StatType::class.java)
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    fun getDamages(): Map<StatType, Double> {
        return damages
    }

    fun setDamages(damages: MutableMap<StatType, Double>) {
        this.damages = damages
    }

    fun addDamage(type: StatType, damage: Double) {
        damages[type] = damage
    }

    companion object {
        val handlerList = HandlerList()
    }
}