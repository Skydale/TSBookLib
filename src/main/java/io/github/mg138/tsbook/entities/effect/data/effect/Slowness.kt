package io.github.mg138.tsbook.entities.effect.data.effect

import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect
import org.bukkit.entity.LivingEntity
import io.github.mg138.tsbook.entities.effect.data.StatusEffect
import org.bukkit.scheduler.BukkitRunnable
import io.github.mg138.tsbook.listener.event.damage.DamageHandler
import io.github.mg138.tsbook.items.data.stat.StatType
import org.bukkit.Bukkit
import org.bukkit.Material
import io.github.mg138.tsbook.entities.effect.EffectHandler
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffect
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import com.comphenix.packetwrapper.WrapperPlayServerEntityLook
import org.bukkit.attribute.AttributeInstance
import io.github.mg138.tsbook.listener.event.damage.utils.StatCalculator
import org.bukkit.attribute.Attribute
import java.util.*
import java.util.function.Consumer

object Slowness : Effect(
    delay = { it.ticks },
    period = { 0 },
    runBefore = lambda@{ target, statusEffect ->
        val attributeInstance = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!
        val old = attributeInstance.baseValue
        attributeInstance.baseValue = StatCalculator.calculateModifier(old, -1 * statusEffect.power)
        return@lambda old
    },
    whenExpire = { target, _, old ->
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue = old as Double
    },
    condition = { _, _ -> true }
)