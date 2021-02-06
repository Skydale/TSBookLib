package io.github.mg138.tsbook.items.data.stat;

import java.util.HashMap;

public class RegisteredPlaceholder {
    private static HashMap<StatType, String> constructPlaceholder() {
        HashMap<StatType, String> map = new HashMap<>();
        map.put(StatType.CHANCE_CRITICAL, "[chance-critical]");
        map.put(StatType.POWER_CRITICAL, "[power-critical]");
        map.put(StatType.DAMAGE_PHYSICAL, "[damage-physical]");
        map.put(StatType.DAMAGE_IGNIS, "[damage-ignis]");
        map.put(StatType.DAMAGE_AER, "[damage-aer]");
        map.put(StatType.DAMAGE_TERRA, "[damage-terra]");
        map.put(StatType.DAMAGE_AQUA, "[damage-aqua]");
        map.put(StatType.DAMAGE_LUMEN, "[damage-lumen]");
        map.put(StatType.DAMAGE_UMBRA, "[damage-umbra]");
        map.put(StatType.DAMAGE_TRUE, "[damage-true]");
        return map;
    }

    public static final HashMap<StatType, String> HOLDER = constructPlaceholder();
}
