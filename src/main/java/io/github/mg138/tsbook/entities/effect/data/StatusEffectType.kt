package io.github.mg138.tsbook.entities.effect.data

enum class StatusEffectType {
    BURNING, BLEEDING, FROZEN, FALL_DAMAGE_RESISTANCE, SLOWNESS, LEVITATION, NAUSEOUS, PARALYSIS;

    companion object {
        val names: List<String> = values().map { it.name }
    }
}