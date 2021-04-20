package io.github.mg138.tsbook.item.data

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.attribute.stat.StatType

class IdentifiedStat(
    private val identification: Identification
) : StatMap() {
    constructor(map: Map<out StatType, Stat>, identification: Identification) : this(identification) {
        super.putAll(map)
    }

    override fun remove(type: StatType): Stat? {
        this.identification.remove(type)
        return super.remove(type)
    }

    fun set(type: StatType, stat: Stat, identification: Float): Stat? {
        this.identification.putIfAbsent(type, identification)
        return super.set(type, stat)
    }

    override operator fun set(type: StatType, stat: Stat) = this.set(type, stat, 0.5F)

    override operator fun get(type: StatType): Stat? {
        return super.get(type)?.let {
            it * this.identification[type]
        }
    }

    override fun getStatOut(type: StatType): Double {
        return super.getStatOut(type) * this.identification[type]
    }

    override fun translate(type: StatType): String? {
        return this[type]
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "${getPercentage(type)}%")
    }

    private fun getPercentage(type: StatType) = (this.identification[type] * 100).toInt()

    override fun iterator(): MutableIterator<MutableMap.MutableEntry<StatType, Stat>> {
        return object : MutableIterator<MutableMap.MutableEntry<StatType, Stat>> {
            val keys = map.keys.iterator()

            override fun hasNext(): Boolean {
                return keys.hasNext()
            }

            override fun next(): MutableMap.MutableEntry<StatType, Stat> {

            }

            override fun remove() {
                this@IdentifiedStat.
            }
        }
    }
}