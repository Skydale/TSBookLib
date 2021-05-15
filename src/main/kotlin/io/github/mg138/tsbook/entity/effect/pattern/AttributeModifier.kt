package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance

abstract class AttributeModifier(
    private val attribute: Attribute,
    private val modified: (Double, EffectProperty) -> Double
) : Effect {
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

    override fun makeEffect(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        val attribute = property.target.getAttribute(attribute)!!

        val old = attribute.baseValue

        attribute.baseValue = modified(old, property)

        return ActiveAttributeModifier(this, property, effectManager, attribute, old)
    }
}