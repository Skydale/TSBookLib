package io.github.mg138.tsbook.data.adapter.bukkit

import com.google.gson.Gson
import io.github.mg138.tsbook.data.DataMap
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

object DataMapTag : PersistentDataType<String, DataMap> {
    private val gson = Gson()

    override fun getPrimitiveType() = String::class.java
    override fun getComplexType() = DataMap::class.java

    override fun toPrimitive(complex: DataMap, context: PersistentDataAdapterContext): String {
        return gson.toJson(complex).also { result ->
            ByteArrayOutputStream().use { bos ->
                ObjectOutputStream(bos).let {
                    it.writeChars(result)
                    it.flush()
                }
            }
        }
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): DataMap {
        return gson.fromJson(primitive, DataMap::class.java)
    }
}