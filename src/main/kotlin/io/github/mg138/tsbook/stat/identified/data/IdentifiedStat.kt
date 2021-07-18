package io.github.mg138.tsbook.stat.identified.data

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.data.StatMap
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.stat.identified.Identified

class IdentifiedStat(
    map: MutableMap<StatType, Stat> = defaultMap(),
    private val identification: Identification
) : StatMap(map), Identified {
    constructor(stats: StatMap, identification: Identification) : this(stats.toMap(), identification)

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