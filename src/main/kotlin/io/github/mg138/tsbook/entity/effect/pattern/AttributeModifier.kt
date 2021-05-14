package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.Status
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import org.bukkit.attribute.Attribute

abstract class AttributeModifier(
    private val attribute: Attribute,
    private val modified: (Double, Status) -> Double
) : Effect {
    override fun makeEffect(status: Status, effectManager: EffectManager): ActiveEffect {
        val attribute = status.target.getAttribute(attribute)!!

        val old = attribute.baseValue

        attribute.baseValue = modified(old, status)

        return object : FlagEffect.ActiveFlagEffect(this, status, effectManager) {
            override fun deactivate() {
                super.deactivate()
                attribute.baseValue = old
            }
        }
    }
}