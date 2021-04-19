package io.github.mg138.tsbook.item.storage

import com.google.gson.JsonSyntaxException
import io.github.mg138.tsbook.Book.Companion.gson
import io.github.mg138.tsbook.item.data.Identification
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

object IdentificationTag : PersistentDataType<String, Identification> {
    override fun getPrimitiveType(): Class<String> {
        return String::class.java
    }

    override fun getComplexType(): Class<Identification> {
        return Identification::class.java
    }

    override fun toPrimitive(
        identification: Identification,
        persistentDataAdapterContext: PersistentDataAdapterContext
    ): String {
        val bos = ByteArrayOutputStream()
        var result: String? = null
        try {
            val oos = ObjectOutputStream(bos)
            result = gson.toJson(identification)
            oos.writeObject(result)
            oos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        assert(result != null)
        return result!!
    }

    override fun fromPrimitive(
        string: String,
        persistentDataAdapterContext: PersistentDataAdapterContext
    ): Identification {
        var result: Identification? = null
        try {
            result = gson.fromJson(string, Identification::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            println("The error string: $string")
        }
        assert(result != null)
        return result!!
    }
}