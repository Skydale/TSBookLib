package io.github.mg138.tsbook.stat.type

import io.github.mg138.tsbook.stat.type.api.StatTypeManager
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.util.MobTypeUtil
import io.github.mg138.tsbook.event.BookDamageEvent
import io.github.mg138.tsbook.config.BookConfig
import io.github.mg138.tsbook.util.PresetUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.lang.Double.min

/*
enum class StatType {
    SPEED_ATTACK,
    CHANCE_POISON,
    POWER_POISON,
    CHANCE_FROZE,
    CHANCE_REFLECT,
    POWER_REFLECT,
    CHANCE_DODGE,
    POWER_DODGE,
    DEFENSE_SPIDER,
    DEFENSE_UNDEAD,
    DEFENSE_MOBS,
    DEFENSE_HELL,
    DEFENSE_UNDERWATER,
    DEFENSE_PLAYER,
    DEFENSE_THUNDER,
    DEFENSE_SLOWNESS,
    DEFENSE_FIRE,
    DEFENSE_DRAIN,
    DEFENSE_LEVITATION,
    DEFENSE_FROZE,
    AFFINITY_ELEMENT;

    override fun toString() = language.attribute.stat.name[this]
        ?: throw IllegalArgumentException("No name set for ${this.name} in the language file!")

    fun getFormat() = language.attribute.stat.format[this]
        ?: throw IllegalArgumentException("No format set for ${this.name} in the language file!")

    fun getIndicator() = language.attribute.stat.indicator[this]
        ?: throw IllegalArgumentException("No indicator set for ${this.name} in the language file!")
}
*/

abstract class StatType(val identifier: String) {
    private fun notAvailable(name: String) = "No $name set for ${this.identifier} in the language file!"

    open fun getName(): String {
        return BookConfig.language.attribute.stat.name[this]
            ?: throw IllegalArgumentException(this.notAvailable("name"))
    }

    open fun getFormat(): String {
        return BookConfig.language.attribute.stat.name[this]
            ?: throw IllegalArgumentException(this.notAvailable("format"))
    }

    override fun toString() = this.identifier

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatType) return false

        return this.identifier == other.identifier
    }

    override fun hashCode() = this.identifier.hashCode()

    object Preset {
        object DamageTypes {
            val DAMAGE_TRUE =
                object : DamageType("DAMAGE_TRUE") {}

            val DAMAGE_PHYSICAL =
                object : DamageType("DAMAGE_PHYSICAL") {}

            val DAMAGE_TERRA =
                object : DamageType("DAMAGE_TERRA") {}

            val DAMAGE_TEMPUS =
                object : DamageType("DAMAGE_TEMPUS") {}

            val DAMAGE_IGNIS =
                object : DamageType("DAMAGE_IGNIS") {}

            val DAMAGE_AQUA =
                object : DamageType("DAMAGE_AQUA") {}

            val DAMAGE_LUMEN =
                object : DamageType("DAMAGE_LUMEN") {}

            val DAMAGE_UMBRA =
                object : DamageType("DAMAGE_UMBRA") {}

            val DAMAGE_NONE =
                object : DamageType("DAMAGE_NONE") {}

            val DAMAGE_BLEED =
                object : DamageType("DAMAGE_BLEED") {}

            val DAMAGE_THUNDER =
                object : DamageType("DAMAGE_THUNDER") {}

            val types = PresetUtil.getObjectPropertiesOfType<DamageType, DamageTypes>()
        }

        object DefenseTypes {
            val DEFENSE_TRUE =
                object : DefenseType.DefenseTypePattern("DEFENSE_TRUE", DamageTypes.DAMAGE_TRUE) {
                    override fun onDamage(it: Double, event: BookDamageEvent) {
                        event.damagerStat.computeIfPresent(damageType) { damage ->
                            damage - it
                        }
                    }
                }

            val DEFENSE_PHYSICAL =
                object : DefenseType.DefenseTypePattern("DEFENSE_PHYSICAL", DamageTypes.DAMAGE_PHYSICAL) {}

            val DEFENSE_TERRA =
                object : DefenseType.DefenseTypePattern("DEFENSE_TERRA", DamageTypes.DAMAGE_TERRA) {}

            val DEFENSE_TEMPUS =
                object : DefenseType.DefenseTypePattern("DEFENSE_TEMPUS", DamageTypes.DAMAGE_TEMPUS) {}

            val DEFENSE_IGNIS =
                object : DefenseType.DefenseTypePattern("DEFENSE_IGNIS", DamageTypes.DAMAGE_IGNIS) {}

            val DEFENSE_AQUA =
                object : DefenseType.DefenseTypePattern("DEFENSE_AQUA", DamageTypes.DAMAGE_AQUA) {}

            val DEFENSE_LUMEN =
                object : DefenseType.DefenseTypePattern("DEFENSE_LUMEN", DamageTypes.DAMAGE_LUMEN) {}

            val DEFENSE_UMBRA =
                object : DefenseType.DefenseTypePattern("DEFENSE_UMBRA", DamageTypes.DAMAGE_UMBRA) {}

            val types = PresetUtil.getObjectPropertiesOfType<DefenseType, DefenseTypes>()
        }

        object ModifierTypes {
            val MODIFIER_ARTHROPOD =
                object : ModifierType.ModifierTypePattern("MODIFIER_ARTHROPOD") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        MobTypeUtil.isArthropod(event.damageEvent.entity.type)
                }

            val MODIFIER_UNDEAD =
                object : ModifierType.ModifierTypePattern("MODIFIER_UNDEAD") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        MobTypeUtil.isUndead(event.damageEvent.entity.type)
                }

            val MODIFIER_MOBS =
                object : ModifierType.ModifierTypePattern("MODIFIER_MOBS") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        MobTypeUtil.isMob(event.damageEvent.entity.type)
                }

            val MODIFIER_HELL =
                object : ModifierType.ModifierTypePattern("MODIFIER_HELL") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        MobTypeUtil.isHellish(event.damageEvent.entity.type)
                }

            val MODIFIER_UNDERWATER =
                object : ModifierType.ModifierTypePattern("MODIFIER_UNDERWATER") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        MobTypeUtil.isWatery(event.damageEvent.entity.type)
                }

            val MODIFIER_PLAYER =
                object : ModifierType.ModifierTypePattern("MODIFIER_PLAYER") {
                    override fun condition(it: Double, event: BookDamageEvent) =
                        event.damageEvent.entity is Player
                }


            val types = PresetUtil.getObjectPropertiesOfType<ModifierType, ModifierTypes>()
        }

        object PowerTypes {
            val POWER_CRITICAL =
                object : PowerType("POWER_CRITICAL") {
                    override fun onDamage(power: Double, event: BookDamageEvent) {
                        event.damagerStat.map { (type, stat) ->
                            if (type is DamageType) {
                                stat * power
                            }
                        }
                    }
                }

            val POWER_DRAIN =
                object : PowerType("POWER_DRAIN") {
                    override fun onDamage(power: Double, event: BookDamageEvent) {
                        val damageEvent = event.damageEvent as? EntityDamageByEntityEvent ?: return
                        val damager = damageEvent.damager as? LivingEntity ?: return
                        val maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: return

                        val healAmount = damageEvent.damage * power
                        damager.health = min(damager.health + healAmount, maxHealth)
                    }
                }

            val POWER_SLOWNESS =
                object : PowerType("POWER_SLOWNESS") {
                    override fun onDamage(power: Double, event: BookDamageEvent) {
                        val entity = event.damageEvent.entity as? LivingEntity ?: return

                        val ticks = (power * 200).toLong()

                        EffectManager.instance.apply(
                            EffectType.Preset.SLOWNESS,
                            entity,
                            power,
                            ticks
                        )
                    }

                }

            val POWER_NAUSEOUS =
                object : PowerType("POWER_NAUSEOUS") {
                    override fun onDamage(power: Double, event: BookDamageEvent) {
                        val entity = event.damageEvent.entity as? LivingEntity ?: return

                        val ticks = (20 * power).toLong()

                        if (ticks >= 20) {
                            EffectManager.instance.apply(
                                EffectType.Preset.NAUSEOUS,
                                entity,
                                0.0,
                                ticks
                            )
                        }
                    }
                }

            val POWER_LEVITATION =
                object : PowerType("POWER_LEVITATION") {
                    override fun onDamage(power: Double, event: BookDamageEvent) {
                        val entity = event.damageEvent.entity as? LivingEntity ?: return

                        val ticks = (20 * power).toLong()

                        if (ticks >= 20) {
                            EffectManager.instance.apply(
                                EffectType.Preset.LEVITATION,
                                entity,
                                0.0,
                                ticks
                            )
                        }
                    }

                }

            val types = PresetUtil.getObjectPropertiesOfType<PowerType, PowerTypes>()
        }

        object ChanceTypes {
            val CHANCE_CRITICAL =
                object : ChanceType.ChanceTypePattern("CHANCE_CRITICAL", PowerTypes.POWER_CRITICAL) {}

            val CHANCE_DRAIN =
                object : ChanceType.ChanceTypePattern("CHANCE_DRAIN", PowerTypes.POWER_DRAIN) {}

            val CHANCE_SLOWNESS =
                object : ChanceType.ChanceTypePattern("CHANCE_SLOWNESS", PowerTypes.POWER_SLOWNESS) {}

            val CHANCE_NAUSEOUS =
                object : ChanceType.ChanceTypePattern("CHANCE_NAUSEOUS", PowerTypes.POWER_NAUSEOUS) {}

            val CHANCE_LEVITATION =
                object : ChanceType.ChanceTypePattern("CHANCE_LEVITATION", PowerTypes.POWER_LEVITATION) {}

            val types = PresetUtil.getObjectPropertiesOfType<ChanceType, ChanceTypes>()
        }

        val types = listOf(
            *DamageTypes.types.toTypedArray(),
            *DefenseTypes.types.toTypedArray(),
            *ModifierTypes.types.toTypedArray(),
            *PowerTypes.types.toTypedArray(),
            *ChanceTypes.types.toTypedArray()
        )

        init {
            println(types)
            types.forEach(StatTypeManager::register)
        }
    }
}