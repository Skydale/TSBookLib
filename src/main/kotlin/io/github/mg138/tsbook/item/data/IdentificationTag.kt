package io.github.mg138.tsbook.item.data

import io.github.mg138.tsbook.Book.Companion.gson
import io.github.mg138.tsbook.item.attribute.stat.identified.data.Identification
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

object IdentificationTag : PersistentDataType<String, Identification> {
    override fun getPrimitiveType() = String::class.java
    override fun getComplexType() = Identification::class.java

    override fun toPrimitive(complex: Identification, context: PersistentDataAdapterContext): String {
        val result = gson.toJson(complex)

        ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).let {
                it.writeObject(result)
                it.flush()
            }
        }

        return result
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): Identification {
        return gson.fromJson(primitive, Identification::class.java)
    }
}