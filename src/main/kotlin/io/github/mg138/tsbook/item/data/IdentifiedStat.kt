package io.github.mg138.tsbook.item.data

import io.github.mg138.tsbook.item.attribute.stat.Stat
import io.github.mg138.tsbook.item.attribute.stat.StatMap
import io.github.mg138.tsbook.item.attribute.stat.StatType
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import java.util.*
import kotlin.math.roundToInt

class Identification : HashMap<StatType, Float>() {
    operator fun set(key: StatType, value: Float): Float? {
        return super.put(key, value)
    }

    override operator fun get(key: StatType): Float {
        super.putIfAbsent(key, 0.5f)
        return super.get(key)!!
    }

    override fun toString(): String {
        val i: Iterator<Map.Entry<StatType, Float>> = entries.iterator()
        if (!i.hasNext()) return "{}"

        val sb = StringBuilder()
        sb.append('{')
        while (true) {
            val e = i.next()
            val key = e.key
            val value = e.value
            sb.append(key.name)
            sb.append('=')
            sb.append(value)
            if (!i.hasNext()) return sb.append('}').toString()
            sb.append(',').append(' ')
        }
    }
}

class IdentifiedStat(
    private val initStats: StatMap,
    val identification: Identification
) : Iterable<Map.Entry<StatType, Stat>> {
    fun getStatOut(type: StatType): Double {
        return initStats.getStatOut(type) * identification[type]
    }

    operator fun get(type: StatType): Stat {
        return initStats[type]!! * identification[type]
    }

    fun translate(type: StatType): String {
        return this[type]
            .applyPlaceholder(type.getFormat())
            .replace("[name]", type.toString())
            .replace("[percentage]", (identification[type] * 100).toInt().toString() + '%')
    }

    override fun iterator(): Iterator<Map.Entry<StatType, Stat>> {
        return object : Iterator<Map.Entry<StatType, Stat>> {
            val types = initStats.keys.iterator()

            override fun hasNext(): Boolean {
                return types.hasNext()
            }

            override fun next(): Map.Entry<StatType, Stat> {
                return types.next().let {
                    AbstractMap.SimpleImmutableEntry<StatType, Stat>(it, this@IdentifiedStat[it])
                }
            }
        }
    }
}