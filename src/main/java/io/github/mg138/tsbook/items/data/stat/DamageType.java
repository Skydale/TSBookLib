package io.github.mg138.tsbook.items.data.stat;

import java.util.HashSet;

public class DamageType {
    private static HashSet<StatType> constructDamageType() {
        HashSet<StatType> damageType = new HashSet<>();
        damageType.add(StatType.DAMAGE_PHYSICAL);
        damageType.add(StatType.DAMAGE_AER);
        damageType.add(StatType.DAMAGE_AQUA);
        damageType.add(StatType.DAMAGE_TERRA);
        damageType.add(StatType.DAMAGE_IGNIS);
        damageType.add(StatType.DAMAGE_UMBRA);
        damageType.add(StatType.DAMAGE_LUMEN);
        damageType.add(StatType.DAMAGE_TRUE);
        return damageType;
    }

    public static final HashSet<StatType> DAMAGES = constructDamageType();
}