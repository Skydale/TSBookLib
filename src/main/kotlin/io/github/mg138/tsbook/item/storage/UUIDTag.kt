package io.github.mg138.tsbook.item.storage

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

object UUIDTag : PersistentDataType<ByteArray, UUID> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<UUID> {
        return UUID::class.java
    }

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext): ByteArray {
        val bb = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(complex.mostSignificantBits)
        bb.putLong(complex.leastSignificantBits)
        return bb.array()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): UUID {
        val bb = ByteBuffer.wrap(primitive)
        val firstLong = bb.long
        val secondLong = bb.long
        return UUID(firstLong, secondLong)
    }
}