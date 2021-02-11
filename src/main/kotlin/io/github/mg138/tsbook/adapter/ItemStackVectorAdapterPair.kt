package io.github.mg138.tsbook.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import dev.reactant.reactant.utils.content.item.itemStackOf
import io.github.mg138.tsbook.players.data.ItemStackVectorWrapper
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type
import java.util.*

@Component
class ItemStackVectorAdapterPair: SystemLevel, TypeAdapterPair {
    class ItemStackVectorTypeAdapter : TypeAdapter<ItemStackVectorWrapper?>() {
        override fun write(writer: JsonWriter, items: ItemStackVectorWrapper?) {
            writer.beginObject()
            writer.name("items")
            writer.beginObject()
            if (items != null) {
                for (i in items.indices) {
                    writer.name(i.toString())
                    val item = items[i]
                    if (item == null || item.type.isAir) writer.nullValue()
                    else writer.value(YamlConfiguration().also { it["itemstack"] = item }.saveToString())
                }
            }
            writer.endObject()
            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemStackVectorWrapper? {
            val items = ItemStackVectorWrapper()

            reader.beginObject()
            if (reader.nextName() != "items") return null
            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.peek() == JsonToken.NAME) {
                    val i = reader.nextName().toInt()
                    items.setSize(i + 1)
                    items[i] = when (reader.peek()) {
                        JsonToken.NULL -> {
                            reader.nextNull()
                            null
                        }
                        else -> {
                            reader
                                .nextString()
                                .let { YamlConfiguration().apply { loadFromString(it) } }
                                .getItemStack("itemstack") ?: itemStackOf(Material.AIR)
                        }
                    }
                }
            }
            reader.endObject()
            reader.endObject()
            return items
        }
    }

    override val type: Type = ItemStackVectorWrapper::class.java
    override val typeAdapter = ItemStackVectorTypeAdapter()
}