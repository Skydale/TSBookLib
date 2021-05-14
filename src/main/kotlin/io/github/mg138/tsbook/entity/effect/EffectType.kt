package io.github.mg138.tsbook.entity.effect

class EffectType(name: String) {
    val name = name.toLowerCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EffectType) return false

        return name == other.name
    }

    override fun hashCode() = name.hashCode()

    override fun toString() = name

    object PresetTypes {
        val bleeding = EffectType("bleeding")
        val burning = EffectType("burning")
        val fallResistance = EffectType("fall_resistance")
        val levitation = EffectType("levitation")
        val nauseous = EffectType("nauseous")
        val paralysis = EffectType("paralysis")
        val slowness = EffectType("slowness")
    }
}