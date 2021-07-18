package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveAttributeModifier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance

abstract class AttributeModifier(
    private val attribute: Attribute
) : Effect {
    abstract fun getModified(old: Double, property: EffectProperty): Double

    override fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        val attribute = property.target.getAttribute(attribute)!!

        val old = attribute.baseValue

        attribute.baseValue = getModified(old, property)

        return ActiveAttributeModifier(this, property, effectManager, attribute, old)
    }
}