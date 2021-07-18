package io.github.mg138.tsbook.entity.effect.pattern.active

import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import org.bukkit.attribute.AttributeInstance

class ActiveAttributeModifier(
    effect: Effect,
    property: EffectProperty,
    effectManager: EffectManager,
    private val attribute: AttributeInstance,
    private val old: Double
) : ActiveFlagEffect(effect, property, effectManager) {
    override fun deactivate() {
        super.deactivate()
        attribute.baseValue = old
    }
}