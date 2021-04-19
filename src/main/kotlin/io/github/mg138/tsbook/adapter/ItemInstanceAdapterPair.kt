package io.github.mg138.tsbook.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.item.data.Identification
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.setting.item.ItemConfig
import java.lang.reflect.Type
import java.util.*

@Component
class ItemInstanceAdapterPair: SystemLevel, TypeAdapterPair {
    class ItemInstanceAdapter : TypeAdapter<ItemInstance>() {
        private var gson: Gson = Gson()

        override fun write(writer: JsonWriter, instance: ItemInstance?) {
            writer.beginObject()

            if (instance != null) {
                writer.name("id")
                writer.value(instance.id)

                if (instance.itemStat != null) {
                    writer.name("iden")
                    writer.value(gson.toJson(instance.itemStat.identification))
                }
            }

            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemInstance? {
            reader.beginObject()

            var id: String? = null
            var identification: Identification? = null

            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    val nextString = reader.nextString()
                    when (reader.nextName()) {
                        "id" -> id = nextString
                        "iden" -> identification = gson.fromJson(nextString, Identification::class.java)
                    }
                }
            }

            reader.endObject()

            if (id == null || identification == null) return null

            val setting = ItemConfig.getItem(id)

            return ItemInstance(
                setting,
                IdentifiedStat.create(setting, identification),
                UUID.randomUUID()
            )
        }
    }

    override val type: Type = ItemInstance::class.java
    override val typeAdapter = ItemInstanceAdapter()
}