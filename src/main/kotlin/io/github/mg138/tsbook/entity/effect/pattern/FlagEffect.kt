package io.github.mg138.tsbook.entity.effect.pattern

import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.Status
import io.github.mg138.tsbook.entity.effect.util.EffectManager


abstract class FlagEffect : Effect {
    open class ActiveFlagEffect(
        effect: Effect,
        status: Status,
        effectManager: EffectManager
    ) : ActiveEffect(effect, status.target, status.duration, 0L, effectManager) {
        override fun tick() = this.deactivate()
    }

    override fun makeEffect(status: Status, effectManager: EffectManager): ActiveEffect {
        return ActiveFlagEffect(this, status, effectManager)
    }
}

