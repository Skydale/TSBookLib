package io.github.mg138.tsbook.listener.event.damage.utils

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.attribute.stat.StatType
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object DamageIndicator {
    private val indicators: MutableList<ArmorStand> = ArrayList()

    fun unload() {
        if (indicators.isEmpty()) return
        indicators.forEach { it.remove() }
        indicators.clear()
    }

    fun displayDamage(damage: Double, type: String, loc: Location) {
        displayDamage(damage, StatType.valueOf(type.toUpperCase()), loc)
    }

    fun displayDamage(damage: Double, type: StatType, loc: Location) {
        val world = loc.world!!
        val r = Random().nextDouble() * Math.PI * 2
        val x = cos(r) / 6
        val z = sin(r) / 6

        val indic = world.spawn(
            loc.add(Vector(x * 2, Random().nextDouble() / 2, z * 2)),
            ArmorStand::class.java
        ) { indicator: ArmorStand ->
            indicators.add(indicator)
            indicator.isInvulnerable = true
            indicator.isVisible = false
            indicator.setGravity(false)
            indicator.isMarker = true
            indicator.isCustomNameVisible = true
            indicator.customName = type.getIndicator() + damage.toInt()
        }

        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                val currentLoc = indic.location
                if (i < 4) {
                    indic.teleport(
                        Location(
                            world,
                            currentLoc.x + x * 2,
                            currentLoc.y + 0.3,
                            currentLoc.z + z * 2
                        )
                    )
                } else {
                    indic.teleport(
                        Location(
                            world,
                            currentLoc.x,
                            currentLoc.y + 0.3,
                            currentLoc.z
                        )
                    )
                }
                if (i > 10) {
                    indicators.remove(indic)
                    indic.remove()
                    cancel()
                }
                i++
            }
        }.runTaskTimer(Book.inst, 0, 3)
    }
}