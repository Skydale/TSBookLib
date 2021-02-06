package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.data.StatusEffect;
import io.github.mg138.tsbook.entities.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.stat.DamageDefenseRelation;
import io.github.mg138.tsbook.items.data.stat.DamageType;
import io.github.mg138.tsbook.items.data.stat.ModifierType;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.entities.EffectHandler;
import io.github.mg138.tsbook.items.ItemInstance;

import io.github.mg138.tsbook.listener.event.utils.CustomDamageEvent;
import io.github.mg138.tsbook.listener.event.utils.DamageHandler;
import io.github.mg138.tsbook.utils.MobType;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EntityDamage implements Listener {
    private static final BukkitAPIHelper mythicMobHelper = MythicMobs.inst().getAPIHelper();
    private static final HashMap<Entity, Long> lastDamageTime = new HashMap<>();

    public static void unload() {
        lastDamageTime.clear();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        lastDamageTime.remove(event.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DamageHandler.damageCD.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (entity instanceof ArmorStand || entity instanceof Item) return;

        LivingEntity livingEntity = (LivingEntity) entity;

        livingEntity.setMaximumNoDamageTicks(0);
        livingEntity.setNoDamageTicks(0);

        if (event.isCancelled()) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        switch (cause) {
            case FIRE_TICK:
            case FIRE:
            case LAVA:
                event.setCancelled(true);

                lastDamageTime.putIfAbsent(entity, System.currentTimeMillis());
                if ((System.currentTimeMillis() - lastDamageTime.get(entity)) > 500) {
                    DamageHandler.simpleDamage(livingEntity, 2);
                    lastDamageTime.put(entity, System.currentTimeMillis());
                }
                return;
            case FALL:
                if (EffectHandler.hasEffect(livingEntity, StatusEffectType.FALL_DAMAGE_RESISTANCE)) {
                    event.setCancelled(true);

                    StatusEffect effect = EffectHandler.getEffect(livingEntity, StatusEffectType.FALL_DAMAGE_RESISTANCE);
                    assert effect != null;
                    DamageHandler.simpleDamage(livingEntity, event.getDamage() * (1 - effect.power));
                    return;
                }
        }
        if (event.isCancelled()) return;

        if (event instanceof EntityDamageByEntityEvent) {
            if (mythicMobHelper.isMythicMob(entity)) {
                ConfigurationSection mob = Book.getCfg().getMMMob(mythicMobHelper.getMythicMobInstance(entity).getType().getInternalName());
                if (mob != null) {
                    HashMap<StatType, Double> map = new HashMap<>();
                    for (String key : mob.getKeys(false)) {
                        map.put(StatType.valueOf(key.toUpperCase()), mob.getDouble(key));
                    }
                    DamageHandler.damagedByEntity((EntityDamageByEntityEvent) event, map);
                    return;
                }
            }
            DamageHandler.damagedByEntity((EntityDamageByEntityEvent) event, new HashMap<>());
        }
    }
}