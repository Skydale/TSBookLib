package io.github.mg138.tsbook.item.storage

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

object UUIDArrayTag : PersistentDataType<ByteArray, Array<UUID>> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<Array<UUID>> {
        return Array<UUID>::class.java
    }

    override fun toPrimitive(complex: Array<UUID>, context: PersistentDataAdapterContext): ByteArray {
        val bb = ByteBuffer.wrap(ByteArray(16 * complex.size))
        for (uuid in complex) {
            bb.putLong(uuid.mostSignificantBits)
            bb.putLong(uuid.leastSignificantBits)
        }
        return bb.array()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): Array<UUID> {
        val uuids: MutableList<UUID> = ArrayList()
        val bb = ByteBuffer.wrap(primitive)
        for (i in 0 until primitive.size / 16) {
            val firstLong = bb.long
            val secondLong = bb.long
            uuids.add(UUID(firstLong, secondLong))
        }
        return uuids.toTypedArray()
    }
}