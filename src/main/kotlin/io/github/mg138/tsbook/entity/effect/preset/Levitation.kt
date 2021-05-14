package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.PotionEffect
import io.github.mg138.tsbook.entity.effect.EffectType
import org.bukkit.potion.PotionEffectType

@Component
class Levitation : PotionEffect(PotionEffectType.LEVITATION) {
    override fun getType() = EffectType.PresetTypes.levitation
}