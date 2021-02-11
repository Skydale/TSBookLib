package io.github.mg138.tsbook.players.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashMap;

import static org.bukkit.Color.fromRGB;

public class Particles {
    public static void spawnAngularParticle(Location location, double alpha, double beta, double offsetX, double offsetY, double offsetZ, HashMap<Vector, Particle> particles) {
        World world = location.getWorld();
        assert world != null;

        double x = Math.cos(alpha) * Math.cos(beta);
        double y = Math.sin(beta);
        double z = Math.sin(alpha) * Math.cos(beta);

        location.add(x + offsetX, y + offsetY, z + offsetZ);
        particles.forEach((vector, particle) -> {
            world.spawnParticle(particle, location.add(vector), 1, new Particle.DustOptions(fromRGB(255, 0, 0), 1));
            location.subtract(vector);
        });
    }
}