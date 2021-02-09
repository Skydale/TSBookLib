package io.github.mg138.tsbook.items.data.stat.map;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumMap;

public class RegisteredPlaceholder {
    private static EnumMap<StatType, String> constructPlaceholder() {
        EnumMap<StatType, String> map = new EnumMap<>(StatType.class);
        map.put(StatType.CHANCE_CRITICAL, "[chance-critical]");
        map.put(StatType.POWER_CRITICAL, "[power-critical]");
        map.put(StatType.DAMAGE_PHYSICAL, "[damage-physical]");
        map.put(StatType.DAMAGE_IGNIS, "[damage-ignis]");
        map.put(StatType.DAMAGE_TEMPUS, "[damage-tempus]");
        map.put(StatType.DAMAGE_TERRA, "[damage-terra]");
        map.put(StatType.DAMAGE_AQUA, "[damage-aqua]");
        map.put(StatType.DAMAGE_LUMEN, "[damage-lumen]");
        map.put(StatType.DAMAGE_UMBRA, "[damage-umbra]");
        map.put(StatType.DAMAGE_TRUE, "[damage-true]");
        map.put(StatType.MODIFIER_PLAYER, "[modifier-player]");
        map.put(StatType.CHANCE_DRAIN, "[chance-drain]");
        map.put(StatType.POWER_DRAIN, "[power-drain]");
        map.put(StatType.CHANCE_SLOWNESS, "[chance-slowness]");
        map.put(StatType.POWER_SLOWNESS, "[power-slowness]");
        map.put(StatType.CHANCE_LEVITATION, "[chance-levitation]");
        map.put(StatType.POWER_LEVITATION, "[power-levitation]");
        map.put(StatType.CHANCE_NAUSEOUS, "[chance-nauseous]");
        map.put(StatType.POWER_NAUSEOUS, "[power-nauseous]");
        map.put(StatType.AFFINITY_ELEMENT, "[affinity-element]");
        return map;
    }

    public static final EnumMap<StatType, String> HOLDER = constructPlaceholder();
}
