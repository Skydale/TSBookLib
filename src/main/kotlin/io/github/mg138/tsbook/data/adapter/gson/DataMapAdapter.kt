package io.github.mg138.tsbook.data.adapter.gson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.data.Data
import io.github.mg138.tsbook.data.DataMap

class DataMapAdapter: SystemLevel, TypeAdapterPair {
    override val type = DataMap::class.java

    override val typeAdapter = object : TypeAdapter<DataMap>() {
        private var gson: Gson = Gson()

        override fun write(writer: JsonWriter, dataMap: DataMap) {
            writer.beginObject()

            dataMap.forEach { (key, map) ->
                writer.name(key)

                writer.beginObject()
                map.forEach { (value, data) ->
                    writer.name(value)
                    writer.jsonValue(gson.toJson(data))
                }
                writer.endObject()
            }

            writer.endObject()
        }

        override fun read(reader: JsonReader): DataMap {
            val dataMap = DataMap()

            reader.beginObject()

            while (reader.hasNext()) {
                val key = reader.nextName()

                reader.beginObject()
                while (reader.peek() != JsonToken.END_OBJECT) {
                    val value = reader.nextName()
                    val data = gson.fromJson(reader.nextString(), Data::class.java)
                    dataMap[key, value] = data
                }
                reader.endObject()
            }

            reader.endObject()

            return dataMap
        }
    }
}