package io.github.mg138.tsbook.listener.event.damage;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import io.github.mg138.tsbook.items.ItemIdentification;
import io.github.mg138.tsbook.items.ItemInstance;
import io.github.mg138.tsbook.items.ItemStats;
import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.items.data.stat.StatMap;
import io.github.mg138.tsbook.items.data.stat.StatSingle;
import io.github.mg138.tsbook.items.data.stat.map.DamageDefenseRelation;
import io.github.mg138.tsbook.items.data.stat.set.*;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.entities.util.MobType;
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent;
import io.github.mg138.tsbook.listener.event.damage.utils.DamageIndicator;
import io.github.mg138.tsbook.listener.event.damage.utils.StatCalculator;
import io.github.mg138.tsbook.players.ArcticPlayerDataService;
import io.github.mg138.tsbook.players.data.PlayerData;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class DamageHandler {
    private static final BukkitAPIHelper mythicMobHelper = MythicMobs.inst().getAPIHelper();
    private static final Random rand = new Random();

    public static final Map<Player, Long> damageCD = new HashMap<>();

    public static void damagedByEntity(EntityDamageByEntityEvent event, Map<StatType, Double> defense) {
        Entity damaged = event.getEntity();
        if (!(damaged instanceof LivingEntity)) return;

        Entity damager = event.getDamager();
        if (mythicMobHelper.isMythicMob(damager)) {
            ConfigurationSection mob = Book.Companion.getCfg().getMMMob(mythicMobHelper.getMythicMobInstance(damager).getType().getInternalName());
            if (mob != null) {
                Map<StatType, StatMap> map = new HashMap<>();
                Map<StatType, Float> identification = new HashMap<>();

                for (String literalType : mob.getKeys(false)) {
                    StatType type = StatType.valueOf(literalType.toUpperCase());
                    map.put(type, new StatMap(type, new StatSingle(mob.getDouble(literalType))));
                    identification.put(type, 1F);
                }
                ItemStats[] stats = { new ItemStats(map, new ItemIdentification(identification), Book.Companion.getCfg()) };
                complexDamage(event, stats, defense);
            }
        } else if (damager instanceof Player) {
            Player player = (Player) damager;
            if (damageCD.putIfAbsent(player, System.currentTimeMillis()) != null) {
                if ((System.currentTimeMillis() - damageCD.get(player)) < 500) {
                    event.setCancelled(true);
                    return;
                }
                damageCD.replace(player, System.currentTimeMillis());
            }

            List<ItemStats> stats = new ArrayList<>();

            EntityEquipment equipment = player.getEquipment();
            if (equipment != null) {
                ItemStack item = equipment.getItemInMainHand();

                if (item.getType() != Material.AIR && item.getItemMeta() != null) {
                    ItemInstance inst = ItemUtils.getInstByItem(Book.inst, item);

                    if (inst != null) {
                        stats.add(inst.getStats());
                    }
                }
            }

            PlayerData data = ArcticPlayerDataService.dataServiceInstance.getData(player, ArcticPlayerDataService.Companion.getPlayerDataRef());
            if (data != null) {
                data.getEquipment().forEach((i, armor) -> stats.add(armor.getStats()));
            }

            complexDamage(event, stats.toArray(new ItemStats[0]), defense);
        } else if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            PersistentDataContainer container = arrow.getPersistentDataContainer();

            UUID[] uuids = container.get(new NamespacedKey(Book.inst, "item_uuids"), ItemUtils.UUID_ARRAY_TAG_TYPE);
            if (uuids == null) return;

            List<ItemStats> stats = new ArrayList<>();

            for (UUID uuid : uuids) {
                ItemInstance inst = ItemUtils.UUID_ITEM.get(uuid);
                if (inst == null) continue;

                stats.add(inst.getStats());
            }

            complexDamage(event, stats.toArray(new ItemStats[0]), defense);
        }
    }

    public static void complexDamage(EntityDamageByEntityEvent event, ItemStats[] stats, Map<StatType, Double> defense) {
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
        for (Map.Entry<StatType, Double> entry : getModifier(stats).entrySet()) {
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

        Map<StatType, Double> damages = getDamage(stats);
        double critDamage = getStat(EnumSet.of(StatType.POWER_CRITICAL), stats).getOrDefault(StatType.POWER_CRITICAL, 0.0);
        double critChance = getStat(EnumSet.of(StatType.CHANCE_CRITICAL), stats).getOrDefault(StatType.CHANCE_CRITICAL, 0.0);

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
                    chance = (0.25 + getStat(EnumSet.of(StatType.AFFINITY_ELEMENT), stats)
                            .getOrDefault(StatType.AFFINITY_ELEMENT, 0.0) / 100);
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

        Map<StatType, Double> effectPower = getEffectPower(stats);
        for (Map.Entry<StatType, Double> entry : getEffectChance(stats).entrySet()) {
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

    private static Map<StatType, Double> getStat(Set<StatType> template, ItemStats[] stats) {
        Map<StatType, Double> result = new HashMap<>();
        for (ItemStats stat : stats) {
            template.forEach(type -> {
                if (stat.getStats().containsKey(type)) {
                    result.put(type, stat.getStat(type) + result.getOrDefault(type, 0.0));
                }
            });
        }
        return result;
    }

    public static Map<StatType, Double> getDefense(ItemStats[] stats) {
        return getStat(DefenseType.DEFENSE, stats);
    }

    public static Map<StatType, Double> getModifier(ItemStats[] stats) {
        return getStat(ModifierType.MODIFIER, stats);
    }

    public static Map<StatType, Double> getDamage(ItemStats[] stats) {
        return getStat(DamageType.DAMAGE, stats);
    }

    public static Map<StatType, Double> getEffectPower(ItemStats[] stats) {
        return getStat(EffectPowerType.EFFECT_POWER, stats);
    }

    public static Map<StatType, Double> getEffectChance(ItemStats[] stats) {
        return getStat(EffectChanceType.EFFECT_CHANCE, stats);
    }

    public static boolean simpleDamage(LivingEntity entity, double damage, StatType damageType, boolean display) {
        if (entity.isDead()) return false;

        double health = entity.getHealth();
        double result = health - damage;
        entity.setHealth(result < 0 ? 0 : result);
        entity.damage(0);

        entity.setMaximumNoDamageTicks(0);
        entity.setNoDamageTicks(0);

        if (display && DamageType.DAMAGE.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.getLocation());
        }

        return true;
    }

    public static boolean simpleDamage(LivingEntity entity, double damage) {
        return simpleDamage(entity, damage, null, false);
    }
}
