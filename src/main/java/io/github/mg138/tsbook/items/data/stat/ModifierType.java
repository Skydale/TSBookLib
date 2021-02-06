package io.github.mg138.tsbook.items.data.stat;

import java.util.HashSet;

public class ModifierType {
    private static HashSet<StatType> constructDamageType() {
        HashSet<StatType> modifierType = new HashSet<>();
        modifierType.add(StatType.MODIFIER_HELL);
        modifierType.add(StatType.MODIFIER_MOBS);
        modifierType.add(StatType.MODIFIER_PLAYER);
        modifierType.add(StatType.MODIFIER_ARTHROPOD);
        modifierType.add(StatType.MODIFIER_UNDEAD);
        modifierType.add(StatType.MODIFIER_UNDERWATER);
        return modifierType;
    }

    public static final HashSet<StatType> MODIFIERS = constructDamageType();
}