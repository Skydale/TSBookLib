package io.github.mg138.tsbook.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.attribute.InternalItemType
import io.github.mg138.tsbook.setting.item.element.ItemSetting
import io.github.mg138.tsbook.setting.item.element.StatedItemSetting
import io.github.mg138.tsbook.item.ItemIdentification
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.item.ItemStat
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
                writer.value(instance.internalItemType.string)

                writer.name("ID")
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
            var internalItemType: InternalItemType? = null
            var id: String? = null
            var identification: ItemIdentification? = null

            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    when (reader.nextName()) {
                        "internal_type" -> internalItemType = InternalItemType.of(reader.nextString())
                        "ID" -> id = reader.nextString()
                        "iden" -> identification = gson.fromJson(reader.nextString(), ItemIdentification::class.java)
                    }
                }
            }
            reader.endObject()

            if (internalItemType == null || id == null || identification == null) return null

            val setting: ItemSetting = when (internalItemType) {
                InternalItemType.ITEM -> ItemConfig.getItem(id)!!
                InternalItemType.UNID -> ItemConfig.getUnid(id)!!
            }

            return if (setting is StatedItemSetting) ItemInstance(
                setting,
                ItemStat(setting.stats, identification),
                internalItemType,
                UUID.randomUUID()
            ) else ItemInstance(
                setting,
                null,
                internalItemType,
                UUID.randomUUID()
            )
        }
    }

    override val type: Type = ItemInstance::class.java
    override val typeAdapter = ItemInstanceAdapter()
}