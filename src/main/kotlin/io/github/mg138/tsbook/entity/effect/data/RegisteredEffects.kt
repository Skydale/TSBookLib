package io.github.mg138.tsbook.entity.effect.data

import io.github.mg138.tsbook.entity.effect.data.effect.*
import java.util.*

object RegisteredEffects {
    private fun constructEffects(): EnumMap<StatusType, (EntityStatus) -> Unit> {
        val effects = EnumMap<StatusType, (EntityStatus) -> Unit>(StatusType::class.java)
        effects[StatusType.BURNING] = Burning.effect
        effects[StatusType.BLEEDING] = Bleeding.effect
        effects[StatusType.FALL_DAMAGE_RESISTANCE] = FallDamageResistance.effect
        effects[StatusType.SLOWNESS] = Slowness.effect
        effects[StatusType.LEVITATION] = Levitation.effect
        effects[StatusType.NAUSEOUS] = Nauseous.effect
        effects[StatusType.PARALYSIS] = Paralysis.effect
        return effects
    }

    val effects = constructEffects()
}