package io.github.mg138.tsbook.items.data.stat.map;

import io.github.mg138.tsbook.items.data.stat.StatType;

import java.util.EnumMap;

public class DamageDefenseRelation {
    private static EnumMap<StatType, StatType> constructRelationship() {
        EnumMap<StatType, StatType> relation = new EnumMap<>(StatType.class);
        relation.put(StatType.DAMAGE_PHYSICAL, StatType.DEFENSE_PHYSICAL);
        relation.put(StatType.DAMAGE_TEMPUS, StatType.DEFENSE_TEMPUS);
        relation.put(StatType.DAMAGE_TERRA, StatType.DEFENSE_TERRA);
        relation.put(StatType.DAMAGE_IGNIS, StatType.DEFENSE_IGNIS);
        relation.put(StatType.DAMAGE_AQUA, StatType.DEFENSE_AQUA);
        relation.put(StatType.DAMAGE_UMBRA, StatType.DEFENSE_UMBRA);
        relation.put(StatType.DAMAGE_LUMEN, StatType.DEFENSE_LUMEN);
        relation.put(StatType.DAMAGE_TRUE, StatType.DEFENSE_TRUE);
        return relation;
    }

    public final static EnumMap<StatType, StatType> relationship = constructRelationship();
}
