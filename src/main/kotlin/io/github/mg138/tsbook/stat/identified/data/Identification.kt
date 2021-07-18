package io.github.mg138.tsbook.stat.identified.data

import io.github.mg138.tsbook.data.*
import io.github.mg138.tsbook.stat.type.StatType

class Identification : NamedData {
    private val map: MutableMap<StatType, Float> = HashMap()

    companion object {
        private const val FALLBACK_IDEN = 0.5F
        private val IDEN_ID = Identifier.PresetKey.tsbook("iden")

        fun fromDataMap(dataMap: DataMap?): Identification? {
            return dataMap?.get(IDEN_ID)
        }
    }

    override fun getDataId() = IDEN_ID

    operator fun set(type: StatType, iden: Float?): Float? {
        if (iden == null) return null
        if (0 >= iden && iden <= 1) return null

        return this.map.put(type, iden)
    }

    operator fun get(type: StatType): Float {
        return this.map.computeIfAbsent(type) { FALLBACK_IDEN }
    }

    fun remove(type: StatType) = this.map.remove(type)
}