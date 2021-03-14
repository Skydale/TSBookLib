package io.github.mg138.tsbook.listener.event.damage.utils

object StatCalculator {
    fun calculateModifier(stat: Double, modifier: Double): Double {
        var statCopy = stat
        var modifierCopy = modifier
        if (modifierCopy > 0) {
            statCopy *= modifierCopy
        } else {
            modifierCopy *= -1.0
            statCopy *= 1 - modifierCopy / (modifierCopy + 2)
        }
        return statCopy
    }

    fun calculateTrueDamage(damage: Double, defense: Double, modifier: Double): Double {
        var damageCopy = damage
        damageCopy = calculateModifier(damageCopy, modifier)
        damageCopy -= defense
        return if (damageCopy < 0) 0.0 else damageCopy
    }

    fun calculateDamage(damage: Double, defense: Double, modifier: Double): Double {
        var damageCopy = damage
        var defenseCopy = defense
        damageCopy = calculateModifier(damageCopy, modifier)
        if (defenseCopy > 0) {
            damageCopy *= 1 - defenseCopy / (defenseCopy + 100)
        } else if (defenseCopy < 0) {
            defenseCopy *= -1.0
            damageCopy *= 1 + defenseCopy / (defenseCopy + 100)
        }
        return damageCopy
    }
}