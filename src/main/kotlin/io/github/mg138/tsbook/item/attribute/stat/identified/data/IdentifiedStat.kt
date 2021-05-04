package io.github.mg138.tsbook.item.attribute.stat.identified.data

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.data.StatMap
import io.github.mg138.tsbook.item.attribute.stat.data.StatType
import io.github.mg138.tsbook.item.attribute.stat.identified.Identified

class IdentifiedStat(
    private val identification: Identification
) : StatMap(), Identified {
    constructor(stats: StatMap, identification: Identification) : this(identification) {
        super.putAll(stats)
    }

    private fun getPercentage(type: StatType) = (this.identification[type] * 100).toInt()
    private fun applyIden(stat: Stat?, type: StatType) = stat?.times(identification[type])

    // Identified

    override fun getIden(type: StatType) = identification[type]
    override fun getIden() = identification

    // Stated

    override fun getStat(type: StatType) = super.getStat(type)?.let { applyIden(it, type) }

    // MutableStated

    fun putStat(type: StatType, stat: Stat, iden: Float?): Stat? {
        this.identification[type] = iden
        return super.putStat(type, stat)
    }

    override fun putStat(type: StatType, stat: Stat) = this.putStat(type, stat, null)
    override fun remove(type: StatType): Stat? {
        this.identification.remove(type)
        return super.remove(type)
    }

    // Translate

    override fun translate(type: StatType) = this.getStat(type)
        ?.applyPlaceholder(type.getFormat())
        ?.replace("[name]", type.toString())
        ?.replace("[percentage]", "${getPercentage(type)}%")
}