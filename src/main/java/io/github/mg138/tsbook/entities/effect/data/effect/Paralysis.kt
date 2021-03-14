package io.github.mg138.tsbook.entities.effect.data.effect

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import io.github.mg138.tsbook.entities.effect.EffectHandler
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import io.github.mg138.tsbook.items.data.stat.StatType
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import java.util.*

object Paralysis : Effect(
    delay = { 0 },
    period = { 1 },
    runBefore = { target, statusEffect ->
        val location = target.location
        location.world!!.strikeLightningEffect(location)
        EffectHandler.apply(StatusEffectType.SLOWNESS, target, 100.0, statusEffect.ticks / 20)
    },
    condition = { _, _ -> false },
    action = lambda@{ i, target, statusEffect ->
        if (i % 4 == 0) {
            return@lambda DamageHandler.simpleDamage(target, statusEffect.power, StatType.DAMAGE_THUNDER, true)
        }

        val loc = target.location
        val yaw = (loc.yaw + 0.5f + 360f) % 360
        Paralysis.look.entityID = target.entityId
        Paralysis.look.yaw = yaw
        Paralysis.look.pitch = loc.pitch
        Paralysis.look.onGround = target.isOnGround
        Paralysis.look.broadcastPacket()
        return@lambda true
    }
) {
    private val look = WrapperPlayServerEntityLook()
}