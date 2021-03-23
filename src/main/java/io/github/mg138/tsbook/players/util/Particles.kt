package io.github.mg138.tsbook.players.util

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

object Particles {
    fun spawnAngularParticle(
        location: Location,
        alpha: Double,
        beta: Double,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        particles: Map<Vector, Particle>
    ) {
        val world = location.world!!
        particles.forEach { (vector, particle) ->
            val m = vector.length()
            val x = m * cos(alpha) * cos(beta)
            val y = m * sin(beta)
            val z = m * sin(alpha) * cos(beta)
            val result = Vector(x + offsetX, y + offsetY, z + offsetZ)
            location.add(result)
            world.spawnParticle(particle, location, 1, DustOptions(Color.fromRGB(255, 0, 0), 1F))
            location.subtract(result)
        }
    }
}