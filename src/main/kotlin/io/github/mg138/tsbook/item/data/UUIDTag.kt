package io.github.mg138.tsbook.item.data

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

object UUIDTag : PersistentDataType<ByteArray, UUID> {
    override fun getPrimitiveType() = ByteArray::class.java
    override fun getComplexType() = UUID::class.java

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(16))

        buffer.putLong(complex.mostSignificantBits)
        buffer.putLong(complex.leastSignificantBits)

        return buffer.array()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): UUID {
        val buffer = ByteBuffer.wrap(primitive)

        val first = buffer.long
        val second = buffer.long

        return UUID(first, second)
    }
}