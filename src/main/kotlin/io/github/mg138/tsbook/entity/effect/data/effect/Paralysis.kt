package io.github.mg138.tsbook.entity.effect.data.effect

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import io.github.mg138.tsbook.stat.StatType
import java.util.*

object Paralysis : SimpleEffectPattern(
    delay = { 0 },
    period = { 1 },
    condition = { _, _ -> false },
    action = lambda@{ i, target, statusEffect ->
        val look = WrapperPlayServerEntityLook()
        val loc = target.location
        val yaw = (loc.yaw + 0.5f + 360f) % 360
        look.entityID = target.entityId
        look.yaw = yaw
        look.pitch = loc.pitch
        look.onGround = target.isOnGround
        look.broadcastPacket()

        if (i % 4 == 0L) {
            return@lambda DamageHandler.simpleDamage(target, statusEffect.power, StatType.DAMAGE_THUNDER, true)
        }
        return@lambda true
    }
)