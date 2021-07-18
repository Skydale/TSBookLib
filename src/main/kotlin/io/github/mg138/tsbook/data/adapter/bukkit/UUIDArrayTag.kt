package io.github.mg138.tsbook.data.adapter.bukkit

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

object UUIDArrayTag : PersistentDataType<ByteArray, Array<UUID>> {
    override fun getPrimitiveType() = ByteArray::class.java
    override fun getComplexType() = Array<UUID>::class.java

    override fun toPrimitive(complex: Array<UUID>, context: PersistentDataAdapterContext): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(16 * complex.size))

        complex.forEach {
            buffer.putLong(it.mostSignificantBits)
            buffer.putLong(it.leastSignificantBits)
        }

        return buffer.array()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): Array<UUID> {
        val size = primitive.size / 16
        val uuids: MutableList<UUID> = ArrayList(size)

        val buffer = ByteBuffer.wrap(primitive)

        for (i in 0 until size) {
            val first = buffer.long
            val second = buffer.long
            uuids[i] = UUID(first, second)
        }

        return uuids.toTypedArray()
    }
}