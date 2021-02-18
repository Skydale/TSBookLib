package io.github.mg138.tsbook.players.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Map;

import static org.bukkit.Color.fromRGB;

public class Particles {
    public static void spawnAngularParticle(Location location, double alpha, double beta, double offsetX, double offsetY, double offsetZ, Map<Vector, Particle> particles) {
        World world = location.getWorld();
        assert world != null;

        particles.forEach((vector, particle) -> {
            double m = vector.length();
            double x = m * Math.cos(alpha) * Math.cos(beta);
            double y = m * Math.sin(beta);
            double z = m * Math.sin(alpha) * Math.cos(beta);
            Vector result = new Vector(x + offsetX, y + offsetY, z + offsetZ);
            location.add(result);
            world.spawnParticle(particle, location, 1, new Particle.DustOptions(fromRGB(255, 0, 0), 1));
            location.subtract(result);
        });
    }
}