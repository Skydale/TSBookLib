package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.stat.util.StatUtil
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.pattern.AttributeModifier
import org.bukkit.attribute.Attribute

@Component
class Slowness : AttributeModifier(
    Attribute.GENERIC_MOVEMENT_SPEED
) {
    override val id = "SLOWNESS"

    override fun getModified(old: Double, property: EffectProperty) =
        StatUtil.damageMod(old, -1 * property.power)
}