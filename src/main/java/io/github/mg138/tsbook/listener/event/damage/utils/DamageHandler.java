package io.github.mg138.tsbook.listener.event.damage.utils;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.stat.StatMap;
import io.github.mg138.tsbook.items.data.stat.StatSingle;
import io.github.mg138.tsbook.items.data.stat.map.DamageDefenseRelation;
import io.github.mg138.tsbook.items.data.stat.set.DamageType;
import io.github.mg138.tsbook.items.data.stat.set.EffectChanceType;
import io.github.mg138.tsbook.items.data.stat.set.EffectPowerType;
import io.github.mg138.tsbook.items.data.stat.set.ModifierType;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.utils.MobType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class DamageHandler {
    private static final Random rand = new Random();
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

            complexDamage(event, stats, defense);
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

            complexDamage(event, stats, defense);
        }
    }

    public static void complexDamage(EntityDamageByEntityEvent event, ItemStats stats, HashMap<StatType, Double> defense) {
        event.setDamage(0);
        LivingEntity livingEntity = (LivingEntity) event.getEntity();

        if (stats == null) {
            simpleDamage(livingEntity, 1);
            return;
        }

        double damage = 0.0;
        Entity entityDamager = event.getDamager();
        LivingEntity livingEntityDamager = null;

        CustomDamageEvent customDamageEvent = null;

        if (entityDamager instanceof LivingEntity) {
            livingEntityDamager = (LivingEntity) entityDamager;
            customDamageEvent = new CustomDamageEvent(livingEntity, livingEntityDamager);
        } else if (entityDamager instanceof Projectile) {
            if (((Projectile) entityDamager).getShooter() instanceof LivingEntity) {
                livingEntityDamager = (LivingEntity) ((Projectile) entityDamager).getShooter();
                customDamageEvent = new CustomDamageEvent(livingEntity, livingEntityDamager);
            }
        }

        double usedModifier = 0.0;
        for (Map.Entry<StatType, Double> entry : getItemModifier(stats).entrySet()) {
            StatType type = entry.getKey();
            Double modifier = entry.getValue();
            switch (type) {
                case MODIFIER_HELL:
                    if (MobType.isHellish(livingEntity.getType())) usedModifier += (modifier / 100);
                    break;
                case MODIFIER_MOBS:
                    if (MobType.isMob(livingEntity.getType())) usedModifier += (modifier / 100);
                    break;
                case MODIFIER_PLAYER:
                    if (livingEntity.getType() == EntityType.PLAYER) usedModifier += (modifier / 100);
                    break;
                case MODIFIER_ARTHROPOD:
                    if (MobType.isArthropod(livingEntity.getType())) usedModifier += (modifier / 100);
                    break;
                case MODIFIER_UNDERWATER:
                    if (MobType.isWatery(livingEntity.getType())) usedModifier += (modifier / 100);
                    break;
                case MODIFIER_UNDEAD:
                    if (MobType.isUndead(livingEntity.getType())) usedModifier += (modifier / 100);
                    break;
            }
        }

        HashMap<StatType, Double> damages = getItemDamage(stats);
        double critDamage = stats.getStat(StatType.POWER_CRITICAL) == null ? 0 : stats.getStat(StatType.POWER_CRITICAL);
        double critChance = stats.getStat(StatType.CHANCE_CRITICAL) == null ? 0 : stats.getStat(StatType.CHANCE_CRITICAL);

        for (StatType damageType : damages.keySet()) {
            double currentDamage;

            if (damageType == StatType.DAMAGE_TRUE) {
                currentDamage = StatCalculator.calculateTrueDamage(
                        damages.get(StatType.DAMAGE_TRUE),
                        defense.getOrDefault(StatType.DEFENSE_TRUE, 0.0),
                        1 + usedModifier
                );
            } else {
                int critical = 0;
                critChance /= 100;
                while (true) {
                    boolean criticalSuccess = (rand.nextFloat() < critChance);
                    if (!criticalSuccess) break;
                    critChance -= 1;
                    critical++;
                }
                currentDamage = StatCalculator.calculateDamage(
                        damages.get(damageType),
                        defense.getOrDefault(DamageDefenseRelation.relationship.get(damageType), 0.0),
                        1 + ((critical * (1 + (critDamage / 100))) - 1) + usedModifier
                );
            }
            damage += currentDamage;
            if (customDamageEvent != null) {
                customDamageEvent.addDamage(damageType, currentDamage);
            }

            double chance = 0.25;
            int x = 0;
            switch (damageType) {
                case DAMAGE_TEMPUS:
                case DAMAGE_AQUA:
                case DAMAGE_IGNIS:
                case DAMAGE_LUMEN:
                case DAMAGE_TERRA:
                case DAMAGE_UMBRA: {
                    chance = (0.25 + (stats.getStats().getOrDefault(
                            StatType.AFFINITY_ELEMENT, new StatMap(
                                    StatType.AFFINITY_ELEMENT, new StatSingle(0.0)
                            )).getValue().getStat() / 100)
                    );
                }
            }

            while (true) {
                boolean success = (rand.nextFloat() < chance);
                if (!success) break;
                chance -= 1;
                x++;
            }

            if (x != 0) {
                switch (damageType) {
                    case DAMAGE_IGNIS: {
                        double power = x * currentDamage / 8;
                        if (power < 20) break;
                        EffectHandler.apply(StatusEffectType.BURNING, livingEntity, power, (int) (currentDamage / 6));
                        break;
                    }
                    case DAMAGE_PHYSICAL: {
                        double power = x * currentDamage / 12;
                        if (power < 20) break;
                        EffectHandler.apply(StatusEffectType.BLEEDING, livingEntity, power, (int) (currentDamage / 14));
                        break;
                    }
                    case DAMAGE_TEMPUS: {
                        double power = x * currentDamage / 8;
                        if (power < 20) break;
                        EffectHandler.apply(StatusEffectType.PARALYSIS, livingEntity, power, (int) (600 * (currentDamage / (currentDamage + 1000))));
                        break;
                    }
                }
            }
        }

        HashMap<StatType, Double> effectPower = getItemEffectPower(stats);
        for (Map.Entry<StatType, Double> entry : getItemEffectChance(stats).entrySet()) {
            StatType type = entry.getKey();
            Double chance = entry.getValue();

            int x = 0;
            chance /= 100;
            while (true) {
                boolean success = (rand.nextFloat() < chance);
                if (!success) break;
                chance -= 1;
                x++;
            }
            if (x == 0) continue;

            switch (type) {
                case CHANCE_DRAIN: {
                    Double power = effectPower.get(StatType.POWER_DRAIN);
                    if (power == null) break;
                    if (livingEntityDamager == null) break;
                    double result = livingEntityDamager.getHealth() + (damage * Math.min(1, (power / 100)));
                    double maxHealth = Objects.requireNonNull(livingEntityDamager.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();

                    livingEntityDamager.setHealth(Math.min(result, maxHealth));
                    break;
                }
                case CHANCE_SLOWNESS: {
                    Double power = effectPower.get(StatType.POWER_SLOWNESS);
                    if (power == null) break;
                    EffectHandler.apply(StatusEffectType.SLOWNESS, livingEntity, x * power / 100, (int) (x * power * 90));
                    break;
                }
                case CHANCE_LEVITATION: {
                    Double power = effectPower.get(StatType.POWER_LEVITATION);
                    if (power == null) break;
                    EffectHandler.apply(StatusEffectType.LEVITATION, livingEntity, 0, (int) (x * power * 20));
                    break;
                }
                case CHANCE_NAUSEOUS: {
                    Double power = effectPower.get(StatType.POWER_NAUSEOUS);
                    if (power == null) break;
                    EffectHandler.apply(StatusEffectType.NAUSEOUS, livingEntity, 0, (int) (x * power * 20));
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
        ModifierType.MODIFIERS.forEach(type -> {
            if (stats.getStats().containsKey(type)) modifiers.put(type, stats.getStat(type));
        });
        return modifiers;
    }

    public static HashMap<StatType, Double> getItemDamage(ItemStats stats) {
        HashMap<StatType, Double> damages = new HashMap<>();
        DamageType.DAMAGES.forEach(type -> {
            if (stats.getStats().containsKey(type)) damages.put(type, stats.getStat(type));
        });
        return damages;
    }

    public static HashMap<StatType, Double> getItemEffectPower(ItemStats stats) {
        HashMap<StatType, Double> effectPower = new HashMap<>();
        EffectPowerType.EFFECT_POWER.forEach(type -> {
            if (stats.getStats().containsKey(type)) effectPower.put(type, stats.getStat(type));
        });
        return effectPower;
    }

    public static HashMap<StatType, Double> getItemEffectChance(ItemStats stats) {
        HashMap<StatType, Double> effectChance = new HashMap<>();
        EffectChanceType.EFFECT_CHANCE.forEach(type -> {
            if (stats.getStats().containsKey(type)) effectChance.put(type, stats.getStat(type));
        });
        return effectChance;
    }

    public static boolean simpleDamage(LivingEntity entity, double damage, StatType damageType, boolean display) {
        if (entity.isDead()) return false;

        double health = entity.getHealth();
        double result = health - damage;
        entity.setHealth(result < 0 ? 0 : result);
        entity.damage(0);

        entity.setMaximumNoDamageTicks(0);
        entity.setNoDamageTicks(0);

        if (display && DamageType.DAMAGES.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.getLocation());
        }

        return true;
    }

    public static boolean simpleDamage(LivingEntity entity, double damage) {
        return simpleDamage(entity, damage, null, false);
    }
}
