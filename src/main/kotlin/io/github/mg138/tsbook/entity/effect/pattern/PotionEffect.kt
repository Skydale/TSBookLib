package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.Status
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import org.bukkit.potion.PotionEffectType

abstract class PotionEffect(
    private val potionType: PotionEffectType,
    private val duration: (Status) -> Int = { 2147483647 },
    private val amplifier: (Status) -> Int = { 0 },
    private val ambient: Boolean = false,
    private val particles: Boolean = false,
    private val icon: Boolean = true
) : Effect {
    class ActivePotionEffect(
        effect: Effect,
        status: Status,
        effectManager: EffectManager,
        private val potionType: PotionEffectType
    ) : FlagEffect.ActiveFlagEffect(effect, status, effectManager) {
        override fun deactivate() {
            super.deactivate()
            entity.removePotionEffect(potionType)
        }
    }

    override fun makeEffect(status: Status, effectManager: EffectManager): ActiveEffect {
        val target = status.target

        target.addPotionEffect(
            org.bukkit.potion.PotionEffect(potionType, duration(status), amplifier(status), ambient, particles, icon)
        )

        return ActivePotionEffect(this, status, effectManager, potionType)
    }
}