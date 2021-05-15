package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import org.bukkit.potion.PotionEffectType

abstract class PotionEffect(
    private val potionType: PotionEffectType,
    private val amplifier: (EffectProperty) -> Int = { 0 },
    private val ambient: Boolean = false,
    private val particles: Boolean = false,
    private val icon: Boolean = true
) : Effect {
    companion object {
        private const val duration = 2147483647
    }

    class ActivePotionEffect(
        effect: Effect,
        property: EffectProperty,
        effectManager: EffectManager,
        private val potionType: PotionEffectType
    ) : ActiveFlagEffect(effect, property, effectManager) {
        override fun deactivate() {
            super.deactivate()
            property.target.removePotionEffect(potionType)
        }
    }

    override fun makeEffect(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        property.target.addPotionEffect(
            org.bukkit.potion.PotionEffect(
                potionType,
                duration,
                amplifier(property),
                ambient,
                particles,
                icon
            )
        )

        return ActivePotionEffect(this, property, effectManager, potionType)
    }
}