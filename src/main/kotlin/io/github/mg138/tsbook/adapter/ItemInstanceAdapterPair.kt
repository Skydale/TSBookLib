package io.github.mg138.tsbook.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.item.ItemIdentified
import io.github.mg138.tsbook.item.attribute.data.Identifier
import io.github.mg138.tsbook.item.attribute.stat.identified.data.Identification
import io.github.mg138.tsbook.item.util.ItemFactory
import io.github.mg138.tsbook.setting.item.ItemConfig
import java.lang.reflect.Type

@Component
class ItemInstanceAdapterPair: SystemLevel, TypeAdapterPair {
    class ItemInstanceAdapter : TypeAdapter<ItemBase>() {
        private var gson: Gson = Gson()

        override fun write(writer: JsonWriter, item: ItemBase?) {
            writer.beginObject()

            if (item != null) {
                writer.name("id")
                writer.value(item.id.toString())

                if (item is ItemIdentified) {
                    writer.name("iden")
                    writer.value(gson.toJson(item.getIden()))
                }
            }

            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemBase? {
            reader.beginObject()

            var id: Identifier? = null
            var iden: Identification? = null

            while (reader.hasNext()) {
                val token = reader.peek()

                if (token == JsonToken.NAME) {
                    val nextString = reader.nextString()

                    when (reader.nextName()) {
                        "id" -> id = Identifier.of(nextString)
                        "iden" -> iden = gson.fromJson(nextString, Identification::class.java)
                    }
                }
            }

            reader.endObject()

            if (id == null || iden == null) return null

            val setting = ItemConfig.getItemWithId(id)

            return ItemFactory.
        }
    }

    override val type: Type = ItemBase::class.java
    override val typeAdapter = ItemInstanceAdapter()
}