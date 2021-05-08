package io.github.mg138.tsbook.entity.effect.preset

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.entity.effect.status.EntityStatus
import io.github.mg138.tsbook.entity.effect.Effect
import io.github.mg138.tsbook.entity.effect.RunningEffect
import io.github.mg138.tsbook.entity.effect.status.StatusType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import org.bukkit.scheduler.BukkitRunnable

@Component
class Paralysis : Effect {
    override fun makeEffect(entityStatus: EntityStatus): RunningEffect {
        val target = entityStatus.target
        val status = entityStatus.status
        val power = status.power
        val duration = status.duration

        val period = 1L

        val look = WrapperPlayServerEntityLook()
        look.entityID = target.entityId

        val runnable = object : BukkitRunnable() {
            var i = 0L

            override fun run() {
                if (i >= duration) {
                    this.cancel(); return
                }

                if (i % 4 == 0L) {
                    if (!DamageHandler.simpleDamage(target, power, StatType.DAMAGE_THUNDER)) {
                        this.cancel(); return
                    }
                }

                val loc = target.location
                look.yaw = (loc.yaw + 0.5f + 360f) % 360
                look.pitch = loc.pitch
                look.onGround = target.isOnGround
                look.broadcastPacket()

                i += period
            }
        }
        return RunningEffect(runnable, 0L, period)
    }

    override fun getType() = StatusType.PresetTypes.paralysis
}