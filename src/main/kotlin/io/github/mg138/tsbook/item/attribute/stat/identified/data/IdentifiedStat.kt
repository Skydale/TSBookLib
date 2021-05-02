package io.github.mg138.tsbook.item.attribute.stat.identified.data

import io.github.mg138.tsbook.item.attribute.stat.Stated
import io.github.mg138.tsbook.item.attribute.stat.identified.Identified
import io.github.mg138.tsbook.item.attribute.stat.data.Stat
import io.github.mg138.tsbook.item.attribute.stat.data.StatMap
import io.github.mg138.tsbook.item.attribute.stat.data.StatType

class IdentifiedStat(
    private val identification: Identification
) : StatMap(), Identified {
    companion object {
        private const val FALLBACK_IDEN = 0.5F
    }

    constructor(stats: Stated, identification: Identification) : this(identification) {
        this.putAll(stats)
    }

    private fun fallbackIden(type: StatType) {
        identification[type] = FALLBACK_IDEN
    }

    private fun getPercentage(type: StatType) = (this.identification[type] * 100).toInt()

    private fun applyIden(stat: Stat?, type: StatType) = stat?.times(identification[type])

    // Stated

    override fun getStat(type: StatType) = super.getStat(type)?.let { applyIden(it, type) }
    override fun getIden(type: StatType) = identification[type]
    override fun getIden() = identification

    // MutableStated

    override fun putStat(type: StatType, stat: Stat) = this.set(type, stat, FALLBACK_IDEN)

    fun set(type: StatType, stat: Stat, iden: Float): Stat? {
        this.identification[type] = iden
        return super.putStat(type, stat)
    }

    override fun remove(type: StatType): Stat? {
        this.identification.remove(type)
        return super.remove(type)
    }

    override fun putAll(stats: Stated) {
        super.putAll(stats)

        if (stats is IdentifiedStat) {
            this.identification.putAll(stats.identification)
        }
    }



    // Translate

    override fun translate(type: StatType) = this.getStat(type)
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "${getPercentage(type)}%")
}