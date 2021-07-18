package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActiveFlagEffect
import io.github.mg138.tsbook.entity.effect.pattern.active.ActivePotionEffect
import org.bukkit.potion.PotionEffectType

abstract class PotionEffect(
    private val potionType: PotionEffectType,
    private val ambient: Boolean = false,
    private val particles: Boolean = false,
    private val icon: Boolean = true
) : Effect {
    companion object {
        private const val duration = 2147483647
    }

    fun getAmplifier(property: EffectProperty): Int = 0

    override fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        property.target.addPotionEffect(
            org.bukkit.potion.PotionEffect(
                potionType,
                duration,
                getAmplifier(property),
                ambient,
                particles,
                icon
            )
        )

        return ActivePotionEffect(this, property, effectManager, potionType)
    }
}