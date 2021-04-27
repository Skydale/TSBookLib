package io.github.mg138.tsbook.item.attribute.stat

import io.github.mg138.tsbook.item.attribute.MutableStated
import io.github.mg138.tsbook.item.attribute.stat.util.StatTables
import java.util.*

open class StatMap() : MutableStated, Iterable<Map.Entry<StatType, Stat>> {
    protected val map = EnumMap<StatType, Stat>(StatType::class.java)

    constructor(map: StatMap) : this() {
        this.putAll(map)
    }

    fun isEmpty() = map.isEmpty()

    fun putAll(map: StatMap) {
        map.forEach { (type, stat) ->
            this.map[type] = stat + this.map[type]
        }
    }

    open fun remove(type: StatType) = map.remove(type)

    override operator fun set(type: StatType, stat: Stat): Stat? = map.put(type, stat)

    override fun getStat(type: StatType): Stat? = map[type]

    override fun getStatOut(type: StatType) = this.getStat(type)?.getStat() ?: 0.0

    open fun applyPlaceholder(string: String, type: StatType): String {
        return StatTables.placeholders[type]?.let {
            this.translate(type)?.let { translated ->
                string.replace(it, translated)
            }
        } ?: string
    }

    open fun translate(type: StatType): String? {
        return this.getStat(type)
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "100%")
    }

    override fun toString(): String {
        return map.toString()
    }

    override fun hashCode(): Int {
        return map.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatMap) return false

        if (map != other.map) return false

        return true
    }

    override fun iterator(): Iterator<Map.Entry<StatType, Stat>> = map.iterator()
}