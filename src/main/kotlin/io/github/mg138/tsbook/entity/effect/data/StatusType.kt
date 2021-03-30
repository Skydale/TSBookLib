package io.github.mg138.tsbook.entity.effect.data

enum class StatusType {
    BURNING, BLEEDING, FROZEN, FALL_DAMAGE_RESISTANCE, SLOWNESS, LEVITATION, NAUSEOUS, PARALYSIS;

    companion object {
        val names = values().map { it.name }
    }
}