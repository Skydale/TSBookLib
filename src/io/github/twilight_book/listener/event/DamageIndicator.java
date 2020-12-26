package io.github.twilight_book.listener.event;

import io.github.twilight_book.Book;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageIndicator {
    private static final List<ArmorStand> indicators = new ArrayList<>();

    public static void unload() {
        if (indicators.isEmpty()) return;

        indicators.forEach(Entity::remove);
        indicators.clear();
    }

    public static void displayDamage(double damage, String type, Location loc) {
        displayDamage(damage, type, loc, false);
    }

    public static void displayDamage(double damage, String type, Location loc, boolean critical) {
        World world = loc.getWorld();
        double r = new Random().nextDouble() * Math.PI * 2;
        double x = Math.cos(r) / 6;
        double z = Math.sin(r) / 6;

        assert world != null;
        ArmorStand indic = world.spawn(loc, ArmorStand.class, indicator -> {
            indicators.add(indicator);
            indicator.setInvulnerable(true);
            indicator.setVisible(false);
            indicator.setGravity(false);
            indicator.setMarker(true);
            indicator.setCustomNameVisible(true);
            indicator.teleport(indicator.getLocation().add(new Vector(x * 2, new Random().nextDouble(), z * 2)));
            indicator.setCustomName(
                    Book.getCfg().getLang().translate("indicator." + type)
                    + (int) damage
                    + (critical ? Book.getCfg().getLang().translate("indicator.critical") : ""
                    )
            );
        });

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                Location currentLoc = indic.getLocation();

                if (i < 4) {
                    indic.teleport(new Location(
                                    world,
                                    currentLoc.getX() + (x * 3),
                                    currentLoc.getY() + 0.3,
                                    currentLoc.getZ() + (z * 3)
                            )
                    );
                } else {
                    indic.teleport(new Location(
                                    world,
                                    currentLoc.getX(),
                                    currentLoc.getY() + 0.3,
                                    currentLoc.getZ()
                            )
                    );
                }
                if (i > 10) {
                    indicators.remove(indic);
                    indic.remove();
                    cancel();
                }
                i++;
            }
        }.runTaskTimer(Book.getInst(), 0, 3);
    }
}