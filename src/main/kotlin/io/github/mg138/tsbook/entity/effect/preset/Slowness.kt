package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.AttributeModifier
import io.github.mg138.tsbook.entity.effect.EffectType
import io.github.mg138.tsbook.item.attribute.stat.util.StatUtil
import org.bukkit.attribute.Attribute

@Component
class Slowness : AttributeModifier(
    Attribute.GENERIC_MOVEMENT_SPEED,
    { old, status ->
        StatUtil.calculateModifier(old, -1 * status.power)
    }
) {
    override fun getType() = EffectType.PresetTypes.slowness
}