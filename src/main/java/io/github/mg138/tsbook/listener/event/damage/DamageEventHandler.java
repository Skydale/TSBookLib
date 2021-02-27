package io.github.mg138.tsbook.listener.event.damage;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.entities.effect.EffectHandler;

import io.github.mg138.tsbook.items.data.stat.set.DefenseType;
import io.github.mg138.tsbook.players.ArcticGlobalDataService;
import io.github.mg138.tsbook.players.data.PlayerData;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamageEventHandler implements Listener {
    private static final BukkitAPIHelper mythicMobHelper = MythicMobs.inst().getAPIHelper();
    private static final Map<Entity, Long> lastDamageTime = new HashMap<>();

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

        switch (event.getCause()) {
            case FIRE_TICK:
            case FIRE:
            case LAVA:
                event.setCancelled(true);

                if ((System.currentTimeMillis() - lastDamageTime.getOrDefault(entity, 0L)) > 500) {
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

        if (event instanceof EntityDamageByEntityEvent) {
            if (mythicMobHelper.isMythicMob(livingEntity)) {
                ConfigurationSection mob = Book.Companion.getCfg().getMMMob(mythicMobHelper.getMythicMobInstance(entity).getType().getInternalName());
                if (mob != null) {
                    Map<StatType, Double> map = new HashMap<>();

                    for (String literalType : mob.getKeys(false)) {
                        StatType type = StatType.valueOf(literalType.toUpperCase());
                        if (DefenseType.DEFENSE.contains(type)) map.put(type, mob.getDouble(literalType));
                    }

                    DamageHandler.damagedByEntity((EntityDamageByEntityEvent) event, map);
                    return;
                }
            } else if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                List<ItemStats> stats = new ArrayList<>();
                PlayerData data = ArcticGlobalDataService.dataServiceInstance.getData(player, ArcticGlobalDataService.Companion.getPlayerDataRef());
                if (data != null) {
                    data.getEquipment().forEach((i, armor) -> stats.add(armor.getStats()));
                }
                DamageHandler.damagedByEntity((EntityDamageByEntityEvent) event, DamageHandler.getDefense(stats.toArray(new ItemStats[0])));
                return;
            }
            DamageHandler.damagedByEntity((EntityDamageByEntityEvent) event, new HashMap<>());
        }
    }
}