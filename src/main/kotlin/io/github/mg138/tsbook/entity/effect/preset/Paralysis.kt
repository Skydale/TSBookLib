package io.github.mg138.tsbook.entity.effect.preset

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.ActiveEffect
import io.github.mg138.tsbook.entity.effect.util.EffectManager
import io.github.mg138.tsbook.entity.effect.Status
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.EffectType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import org.bukkit.entity.LivingEntity

@Component
class Paralysis : Effect {
    fun paralysisEffect(entity: LivingEntity, look: WrapperPlayServerEntityLook) {
        val loc = entity.location
        look.yaw = (loc.yaw + 0.5f + 360f) % 360
        look.pitch = loc.pitch
        look.onGround = entity.isOnGround
        look.broadcastPacket()
    }

    override fun makeEffect(status: Status, effectManager: EffectManager): ActiveEffect {
        return object : ActiveEffect(this, status.target, 0L, 1L, effectManager) {
            val power = status.power
            val duration = status.duration

            val look = WrapperPlayServerEntityLook().also {
                it.entityID = entity.entityId
            }

            var i = 0L

            override fun tick() {
                if (i >= duration) {
                    cancelAndRemove()
                    return
                }

                if (i % 4 == 0L) {
                    if (!DamageHandler.simpleDamage(entity, power, StatType.DAMAGE_THUNDER)) {
                        cancelAndRemove()
                        return
                    }
                }

                paralysisEffect(entity, look)

                i += period
            }
        }
    }

    override fun getType() = EffectType.PresetTypes.paralysis
}