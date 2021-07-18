package io.github.mg138.tsbook.entity.effect.preset

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import org.bukkit.entity.LivingEntity

/*
@Component
class Paralysis : Effect {
    override val id = "PARALYSIS"

    companion object {
        fun paralysisEffect(entity: LivingEntity, look: WrapperPlayServerEntityLook) {
            val loc = entity.location
            look.yaw = (loc.yaw + 0.5f + 360f) % 360
            look.pitch = loc.pitch
            look.onGround = entity.isOnGround
            look.broadcastPacket()
        }
    }

    private class ActiveParalysisEffect(
        effect: Effect,
        property: EffectProperty,
        effectManager: EffectManager
    ) : ActiveEffect(effect, property, 1L, 0L, effectManager) {
        val power = property.power
        val duration = property.duration

        val target = property.target

        val look = WrapperPlayServerEntityLook().also {
            it.entityID = target.entityId
        }

        var i = 0L
        override fun tick() {
            if (i >= duration) {
                stop()
                return
            }

            if (i % 4 == 0L) {
                if (!DamageHandler.simpleDamage(target, power, StatType.DAMAGE_THUNDER)) {
                    stop()
                    return
                }
            }

            paralysisEffect(target, look)

            i += period
        }
    }

    override fun activate(property: EffectProperty, effectManager: EffectManager): ActiveEffect {
        return ActiveParalysisEffect(this, property, effectManager)
    }
}
 */