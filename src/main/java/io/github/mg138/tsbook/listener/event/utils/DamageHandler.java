package io.github.mg138.tsbook.listener.event.utils;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.EffectHandler;
import io.github.mg138.tsbook.entities.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.stat.DamageDefenseRelation;
import io.github.mg138.tsbook.items.data.stat.DamageType;
import io.github.mg138.tsbook.items.data.stat.ModifierType;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.utils.MobType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DamageHandler {
    public static final HashMap<Player, Long> damageCD = new HashMap<>();

    public static void damagedByEntity(EntityDamageByEntityEvent event, HashMap<StatType, Double> defense) {
        Entity damaged = event.getEntity();
        if (!(damaged instanceof LivingEntity)) return;

        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            if (damageCD.putIfAbsent(player, System.currentTimeMillis()) != null) {
                if ((System.currentTimeMillis() - damageCD.get(player)) < 500) {
                    event.setCancelled(true);
                    return;
                }
                damageCD.replace(player, System.currentTimeMillis());
            }

            EntityEquipment equipment = player.getEquipment();
            if (equipment == null) return;

            ItemStack item = equipment.getItemInMainHand();
            if (item.getType() == Material.AIR || item.getItemMeta() == null) return;

            ItemInstance inst = ItemUtils.getInstByItem(Book.getInst(), item, "item");
            if (inst == null) return;

            ItemStats stats = inst.getStats();
            if (stats == null) {
                event.setDamage(0);
                simpleDamage((LivingEntity) damaged, 1);
                return;
            }

            Double nullableCritChance = stats.getStat(StatType.CHANCE_CRITICAL);
            double critChance = nullableCritChance == null ? 0 : nullableCritChance;

            Double nullableCritDamage = stats.getStat(StatType.POWER_CRITICAL);
            double critDamage = nullableCritDamage == null ? 0 : nullableCritDamage;

            complexDamage(event, critChance, critDamage, getItemModifier(stats), getItemDamage(stats), defense);
            return;
        }

        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            PersistentDataContainer container = arrow.getPersistentDataContainer();

            UUID uuid = container.get(new NamespacedKey(Book.getInst(), "item_uuid"), ItemUtils.UUID_TAG_TYPE);
            if (uuid == null) return;

            ItemInstance inst = ItemUtils.UUID_ITEM.get(uuid);
            if (inst == null) return;

            ItemStats stats = inst.getStats();
            if (stats == null) {
                event.setDamage(0);
                simpleDamage((LivingEntity) damaged, 1);
                return;
            }

            Double nullableCritChance = stats.getStat(StatType.CHANCE_CRITICAL);
            double critChance = nullableCritChance == null ? 0 : nullableCritChance;

            Double nullableCritDamage = stats.getStat(StatType.POWER_CRITICAL);
            double critDamage = nullableCritDamage == null ? 0 : nullableCritDamage;

            complexDamage(
                    event,
                    critChance,
                    critDamage,
                    getItemModifier(stats),
                    getItemDamage(stats),
                    defense
            );
        }
    }

    public static void complexDamage(EntityDamageByEntityEvent event, double critChance, double critDamage, HashMap<StatType, Double> modifiers, HashMap<StatType, Double> damages, HashMap<StatType, Double> defense) {
        event.setDamage(0);

        double damage = 0.0;
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        Entity entityDamager = event.getDamager();

        CustomDamageEvent customDamageEvent = null;
        if (entityDamager instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            customDamageEvent = new CustomDamageEvent(livingEntity, damager);
        } else if (entityDamager instanceof Projectile) {
            if (((Projectile) entityDamager).getShooter() instanceof LivingEntity) {
                customDamageEvent = new CustomDamageEvent(livingEntity, (LivingEntity) ((Projectile) entityDamager).getShooter());
            }
        }

        double usedModifier = 0.0;
        for (Map.Entry<StatType, Double> entry : modifiers.entrySet()) {
            StatType type = entry.getKey();
            Double modifier = entry.getValue();
            switch (type) {
                case MODIFIER_HELL:
                    if (MobType.isHellish(livingEntity.getType())) usedModifier += (modifier / 100);
                case MODIFIER_MOBS:
                    if (MobType.isMob(livingEntity.getType())) usedModifier += (modifier / 100);
                case MODIFIER_PLAYER:
                    if (livingEntity.getType() == EntityType.PLAYER) usedModifier += (modifier / 100);
                case MODIFIER_ARTHROPOD:
                    if (MobType.isArthropod(livingEntity.getType())) usedModifier += (modifier / 100);
                case MODIFIER_UNDERWATER:
                    if (MobType.isWatery(livingEntity.getType())) usedModifier += (modifier / 100);
                case MODIFIER_UNDEAD:
                    if (MobType.isUndead(livingEntity.getType())) usedModifier += (modifier / 100);
            }
        }

        for (StatType damageType : damages.keySet()) {
            double currentDamage;

            if (damageType == StatType.DAMAGE_TRUE) {
                currentDamage = DamageCalculator.calculateTrueDamage(
                        damages.get(StatType.DAMAGE_TRUE),
                        defense.getOrDefault(StatType.DEFENSE_TRUE, 0.0),
                        1 + usedModifier
                );
            } else {
                boolean critical = (new Random().nextFloat() < (critChance / 100));
                currentDamage = DamageCalculator.calculateDamage(
                        damages.get(damageType),
                        defense.getOrDefault(DamageDefenseRelation.relationship.get(damageType), 0.0),
                        1 + (critical ? critDamage / 100 : 0) + usedModifier
                );
            }
            damage += currentDamage;
            if (customDamageEvent != null) {
                customDamageEvent.addDamage(damageType, currentDamage);
            }

            if (new Random().nextFloat() < 0.45) {
                double power;
                switch (damageType) {
                    case DAMAGE_IGNIS:
                        power = currentDamage / 8;
                        if (power < 5) break;
                        EffectHandler.apply(StatusEffectType.BURNING, livingEntity, power, (int) (currentDamage / 6));
                        break;
                    case DAMAGE_PHYSICAL:
                        power = currentDamage / 12;
                        if (power < 5) break;
                        EffectHandler.apply(StatusEffectType.BLEEDING, livingEntity, power, (int) (currentDamage / 14));
                        break;
                }
            }
        }
        double health = livingEntity.getHealth();
        double result = health - damage;
        livingEntity.setHealth(result < 0 ? 0 : result);

        livingEntity.setMaximumNoDamageTicks(0);
        livingEntity.setNoDamageTicks(0);

        if (customDamageEvent != null) {
            Bukkit.getPluginManager().callEvent(customDamageEvent);
        }
    }

    public static HashMap<StatType, Double> getItemModifier(ItemStats stats) {
        HashMap<StatType, Double> modifiers = new HashMap<>();
        Supplier<Stream<StatType>> modifierTypes = ModifierType.MODIFIERS::stream;

        stats.getStats().forEach((type, statMap) -> {
            if (modifierTypes.get().anyMatch(modifierType -> modifierType.equals(type))) {
                modifiers.put(type, stats.getStat(type));
            }
        });
        return modifiers;
    }

    public static HashMap<StatType, Double> getItemDamage(ItemStats stats) {
        HashMap<StatType, Double> damages = new HashMap<>();
        Supplier<Stream<StatType>> damageTypes = DamageType.DAMAGES::stream;

        stats.getStats().forEach((type, statMap) -> {
            if (damageTypes.get().anyMatch(damageType -> damageType.equals(type))) {
                damages.put(type, stats.getStat(type));
            }
        });
        return damages;
    }

    public static boolean simpleDamage(LivingEntity entity, double damage) {
        if (entity.isDead()) return false;

        double health = entity.getHealth();
        double result = health - damage;
        entity.setHealth(result < 0 ? 0 : result);
        entity.damage(0);

        entity.setMaximumNoDamageTicks(0);
        entity.setNoDamageTicks(0);
        return true;
    }
}
