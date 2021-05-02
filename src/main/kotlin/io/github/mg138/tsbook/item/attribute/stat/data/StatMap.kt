package io.github.mg138.tsbook.item.attribute.stat.data

import io.github.mg138.tsbook.item.attribute.stat.MutableStated
import io.github.mg138.tsbook.item.attribute.stat.Stated
import io.github.mg138.tsbook.item.attribute.stat.util.StatTables
import java.lang.IllegalArgumentException
import java.util.*

open class StatMap() : MutableStated {
    protected val map = EnumMap<StatType, Stat>(StatType::class.java)

    constructor(stats: Stated) : this() {
        this.putAll(stats)
    }

    fun isEmpty() = map.isEmpty()

    // Stated

    override fun types() = map.keys
    override fun stats() = map.values
    override fun getStatOut(type: StatType) = this.getStat(type)?.getStat() ?: 0.0
    override fun getStat(type: StatType) = map[type]

    // MutableStated

    override fun putStat(type: StatType, stat: Stat) = map.put(type, stat)
    override fun remove(type: StatType) = map.remove(type)
    override fun putAll(stats: Stated) {
        stats.forEach { (type, stat) ->
            this.putStat(type, stat)
        }
    }
    override fun addAll(stats: Stated) {
        stats.forEach { (type, stat) ->
            this.putStat(type, stat + this.getStat(type))
        }
    }

    // Translate

    open fun applyPlaceholder(string: String, type: StatType): String {
        return StatTables.placeholders[type]?.let {
            this.translate(type)?.let { translated ->
                string.replace(it, translated)
            }
        } ?: string
    }
    open fun translate(type: StatType) = this.getStat(type)
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "100%")

    // Any

    override fun toString() = map.toString()
    override fun hashCode() = map.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is StatMap) return false

        if (map != other.map) return false

        return true
    }

    // Iterable

    override fun iterator() = object : Iterator<Pair<StatType, Stat>> {
        val keys = map.keys.iterator()

        override fun hasNext() = keys.hasNext()

        override fun next() = keys.next().let { type ->
            getStat(type)?.let {
                Pair(type, it)
            } ?: throw IllegalArgumentException("${this@StatMap::class.simpleName} doesn't contain ${type.name}!")
        }
    }
}