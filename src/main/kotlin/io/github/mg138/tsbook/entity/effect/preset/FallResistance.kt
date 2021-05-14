package io.github.mg138.tsbook.entity.effect.preset

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.pattern.FlagEffect
import io.github.mg138.tsbook.entity.effect.EffectType

@Component
class FallResistance : FlagEffect() {
    override fun getType() = EffectType.PresetTypes.fallResistance
}