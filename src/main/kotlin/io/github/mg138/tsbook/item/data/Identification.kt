package io.github.mg138.tsbook.item.data

import io.github.mg138.tsbook.item.attribute.stat.StatType
import java.util.HashMap

class Identification : HashMap<StatType, Float>() {
    operator fun set(key: StatType, value: Float): Float? {
        return super.put(key, value)
    }

    override operator fun get(key: StatType): Float {
        super.putIfAbsent(key, 0.5F)
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