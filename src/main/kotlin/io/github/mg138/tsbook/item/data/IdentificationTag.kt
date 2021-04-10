package io.github.mg138.tsbook.item.data

import com.google.gson.JsonSyntaxException
import io.github.mg138.tsbook.Book.Companion.gson
import io.github.mg138.tsbook.item.ItemIdentification
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

object IdentificationTag : PersistentDataType<String, ItemIdentification> {
    override fun getPrimitiveType(): Class<String> {
        return String::class.java
    }

    override fun getComplexType(): Class<ItemIdentification> {
        return ItemIdentification::class.java
    }

    override fun toPrimitive(
        itemIdentification: ItemIdentification,
        persistentDataAdapterContext: PersistentDataAdapterContext
    ): String {
        val bos = ByteArrayOutputStream()
        var result: String? = null
        try {
            val oos = ObjectOutputStream(bos)
            result = gson.toJson(itemIdentification)
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
    ): ItemIdentification {
        var result: ItemIdentification? = null
        try {
            result = gson.fromJson(string, ItemIdentification::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            println("The error string: $string")
        }
        assert(result != null)
        return result!!
    }
}