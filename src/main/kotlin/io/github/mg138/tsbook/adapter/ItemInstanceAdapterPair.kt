package io.github.mg138.tsbook.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import io.github.mg138.tsbook.items.ItemIdentification
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.setting.BookConfig
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
                writer.name("internal_type")
                writer.value(instance.internalType)

                writer.name("ID")
                writer.value(instance.id)

                if (instance.itemStats != null) {
                    writer.name("iden")
                    writer.value(gson.toJson(instance.itemStats.identification))
                }
            }
            writer.endObject()
        }

        override fun read(reader: JsonReader): ItemInstance? {
            reader.beginObject()
            var internalType: String? = null
            var id: String? = null
            var identification: ItemIdentification? = null

            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    when (reader.nextName()) {
                        "internal_type" -> internalType = reader.nextString()
                        "ID" -> id = reader.nextString()
                        "iden" -> identification = gson.fromJson(reader.nextString(), ItemIdentification::class.java)
                    }
                }
            }
            reader.endObject()

            if (internalType == null || id == null || identification == null) return null

            var setting: ItemSetting? = null
            when (internalType) {
                "item" -> setting = ItemConfig.getItem(id)
                "unid" -> setting = ItemConfig.getUnid(id)
            }
            if (setting == null) return null

            return if (setting is StatedItemSetting) ItemInstance(
                setting,
                ItemStats(setting.stats, identification, BookConfig),
                internalType,
                UUID.randomUUID()
            ) else ItemInstance(
                setting,
                null,
                internalType,
                UUID.randomUUID()
            )
        }
    }

    override val type: Type = ItemInstance::class.java
    override val typeAdapter = ItemInstanceAdapter()
}