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
import io.github.mg138.tsbook.data.DataMap
import io.github.mg138.tsbook.data.Identifier
import io.github.mg138.tsbook.item.api.ItemManager

/*
@Component
class ItemBaseAdapter(
    private val itemManager: ItemManager
): SystemLevel, TypeAdapterPair {
    override val type = ItemBase::class.java

    override val typeAdapter = object : TypeAdapter<ItemBase>() {
        private var gson: Gson = Gson()

        override fun write(writer: JsonWriter, item: ItemBase) {
            writer.beginObject()

            writer.name("id")
            writer.value(item.getItemId().toString())

            writer.name("data")
            writer.jsonValue(gson.toJson(item.getDataMap()))

            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemBase? {
            var id: Identifier? = null
            var dataMap: DataMap? = null

            reader.beginObject()

            while (reader.hasNext()) {
                val token = reader.peek()

                if (token == JsonToken.NAME) {
                    val nextString = reader.nextString()

                    when (reader.nextName()) {
                        "id" -> id = Identifier.of(nextString)
                        "data" -> dataMap = gson.fromJson(nextString, DataMap::class.java)
                    }
                }
            }

            reader.endObject()

            if (id == null) return null

            return itemManager.generateItem(id, dataMap)
        }
    }
}

 */