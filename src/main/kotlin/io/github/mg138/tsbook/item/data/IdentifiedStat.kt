package io.github.mg138.tsbook.item.data

import io.github.mg138.tsbook.item.attribute.Identified
import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.attribute.stat.StatType

class IdentifiedStat (
    private val identification: Identification
) : StatMap(), Identified {
    constructor(map: StatMap, identification: Identification) : this(identification) {
        super.putAll(map)
    }

    private fun getPercentage(type: StatType) = (this.identification[type] * 100).toInt()

    private fun applyIden(stat: Stat?, type: StatType) = stat?.times(identification[type])

    override fun remove(type: StatType): Stat? {
        this.identification.remove(type)
        return super.remove(type)
    }

    fun set(type: StatType, stat: Stat, identification: Float): Stat? {
        this.identification[type] = identification
        return super.set(type, stat)
    }

    override operator fun set(type: StatType, stat: Stat) = this.set(type, stat, 0.5F)

    override fun getIden(type: StatType) = identification[type]

    override fun getIden(): Identification = identification

    override fun getStat(type: StatType): Stat? {
        return super.getStat(type)?.let {
            applyIden(it, type)
        }
    }

    override fun translate(type: StatType): String? {
        return this.getStat(type)
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "${getPercentage(type)}%")
    }

    override fun iterator() = object : Iterator<Map.Entry<StatType, Stat>> {
        val iter = map.iterator()

        override fun hasNext(): Boolean {
            return iter.hasNext()
        }

        override fun next(): Map.Entry<StatType, Stat> {
            return iter.next().also {
                it.setValue(it.value * identification[it.key])
            }
        }
    }
}