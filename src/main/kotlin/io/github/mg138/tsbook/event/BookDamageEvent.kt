package io.github.mg138.tsbook.event

import io.github.mg138.tsbook.stat.data.StatMap
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent

class BookDamageEvent(val damageEvent: EntityDamageEvent, val damagedStat: StatMap, val damagerStat: StatMap) : Event() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}