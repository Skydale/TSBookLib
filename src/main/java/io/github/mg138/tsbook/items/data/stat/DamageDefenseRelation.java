package io.github.mg138.tsbook.items.data.stat;

import java.util.HashMap;

public class DamageDefenseRelation {
    private static HashMap<StatType, StatType> constructRelationship() {
        HashMap<StatType, StatType> relation = new HashMap<>();
        relation.put(StatType.DAMAGE_PHYSICAL, StatType.DEFENSE_PHYSICAL);
        relation.put(StatType.DAMAGE_AER, StatType.DEFENSE_AER);
        relation.put(StatType.DAMAGE_TERRA, StatType.DEFENSE_TERRA);
        relation.put(StatType.DAMAGE_IGNIS, StatType.DEFENSE_IGNIS);
        relation.put(StatType.DAMAGE_AQUA, StatType.DEFENSE_AQUA);
        relation.put(StatType.DAMAGE_UMBRA, StatType.DEFENSE_UMBRA);
        relation.put(StatType.DAMAGE_LUMEN, StatType.DEFENSE_LUMEN);
        relation.put(StatType.DAMAGE_TRUE, StatType.DEFENSE_TRUE);
        return relation;
    }

    public final static HashMap<StatType, StatType> relationship = constructRelationship();
}
