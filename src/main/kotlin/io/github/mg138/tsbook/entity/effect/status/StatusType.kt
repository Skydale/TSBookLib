package io.github.mg138.tsbook.entity.effect.status

class StatusType(name: String) {
    val name = name.toLowerCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatusType) return false

        return name == other.name
    }

    override fun hashCode() = name.hashCode()

    override fun toString() = name

    object PresetTypes {
        val bleeding = StatusType("bleeding")
        val burning = StatusType("burning")
        val fallResistance = StatusType("fall_resistance")
        val levitation = StatusType("levitation")
        val nauseous = StatusType("nauseous")
        val paralysis = StatusType("paralysis")
        val slowness = StatusType("slowness")
    }
}