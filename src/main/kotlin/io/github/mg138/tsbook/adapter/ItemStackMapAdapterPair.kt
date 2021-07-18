package io.github.mg138.tsbook.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import dev.reactant.reactant.utils.content.item.itemStackOf
import io.github.mg138.tsbook.player.data.ItemStackMapWrapper
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

//todo use anonymous object

@Component
class ItemStackMapAdapterPair: SystemLevel, TypeAdapterPair {
    override val type = ItemStackMapWrapper::class.java
    override val typeAdapter = object : TypeAdapter<ItemStackMapWrapper?>() {
        override fun write(writer: JsonWriter, items: ItemStackMapWrapper?) {
            writer.beginObject()
            writer.name("items")
            writer.beginObject()
            if (items != null) {
                for (i in items.entries) {
                    writer.name(i.key.toString())
                    writer.value(YamlConfiguration().also { it["itemstack"] = i.value }.saveToString())
                }
            }
            writer.endObject()
            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemStackMapWrapper? {
            val items = ItemStackMapWrapper()

            reader.beginObject()
            if (reader.nextName() != "items") return null
            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.peek() == JsonToken.NAME) {
                    val i = reader.nextName().toInt()
                    items[i] = reader
                        .nextString()
                        .let { YamlConfiguration().apply { loadFromString(it) } }
                        .getItemStack("itemstack") ?: itemStackOf(Material.AIR)
                }
            }
            reader.endObject()
            reader.endObject()
            return items
        }
    }
}