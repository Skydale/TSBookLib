package io.github.mg138.tsbook.entities.effect.data.effect

import io.github.mg138.tsbook.stat.util.StatUtil
import org.bukkit.attribute.Attribute
import java.util.*

object Slowness : Effect(
    delay = { it.ticks },
    period = { 0 },
    runBefore = lambda@{ target, statusEffect ->
        val attributeInstance = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!
        val old = attributeInstance.baseValue
        attributeInstance.baseValue = StatUtil.calculateModifier(old, -1 * statusEffect.power)
        return@lambda old
    },
    whenExpire = { target, _, old ->
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue = old as Double
    },
    condition = { _, _ -> true }
)