package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.PotionEffect
import org.bukkit.potion.PotionEffectType

@Component
class Nauseous : PotionEffect(PotionEffectType.CONFUSION) {
    override val id = "NAUSEOUS"
}