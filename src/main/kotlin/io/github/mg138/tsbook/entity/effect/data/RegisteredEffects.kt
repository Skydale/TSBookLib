package io.github.mg138.tsbook.entity.effect.data

import io.github.mg138.tsbook.entity.effect.data.effect.*
import java.util.*

object RegisteredEffects {
    private fun constructEffects(): EnumMap<StatusEffectType, (EntityStatusEffect) -> Unit> {
        val effects = EnumMap<StatusEffectType, (EntityStatusEffect) -> Unit>(StatusEffectType::class.java)
        effects[StatusEffectType.BURNING] = Burning.effect
        effects[StatusEffectType.BLEEDING] = Bleeding.effect
        effects[StatusEffectType.FALL_DAMAGE_RESISTANCE] = FallDamageResistance.effect
        effects[StatusEffectType.SLOWNESS] = Slowness.effect
        effects[StatusEffectType.LEVITATION] = Levitation.effect
        effects[StatusEffectType.NAUSEOUS] = Nauseous.effect
        effects[StatusEffectType.PARALYSIS] = Paralysis.effect
        return effects
    }

    val Effects = constructEffects()
}