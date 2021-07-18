package io.github.mg138.tsbook.entity.effect.pattern.active

import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import org.bukkit.potion.PotionEffectType

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