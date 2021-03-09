package io.github.mg138.tsbook.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.dependency.layers.SystemLevel
import dev.reactant.reactant.extra.parser.gsonadapters.TypeAdapterPair
import io.github.mg138.tsbook.Book.Companion.cfg
import io.github.mg138.tsbook.items.ItemIdentification
import io.github.mg138.tsbook.items.ItemInstance
import io.github.mg138.tsbook.config.item.element.ItemSetting
import io.github.mg138.tsbook.items.ItemStats
import io.github.mg138.tsbook.config.AbstractConfig
import org.bukkit.configuration.file.YamlConfiguration
import java.lang.reflect.Type
import java.util.*


@Component
class ItemInstanceAdapterPair: SystemLevel, TypeAdapterPair {
    class ItemInstanceAdapter(private var config: AbstractConfig) : TypeAdapter<ItemInstance>() {
        private var gson: Gson = Gson()

        override fun write(writer: JsonWriter, instance: ItemInstance?) {
            writer.beginObject()

            if (instance != null) {
                writer.name("internal_type")
                writer.value(instance.internalType)

                writer.name("ID")
                writer.value(instance.id)

                if (instance.stats != null) {
                    writer.name("iden")
                    writer.value(gson.toJson(instance.stats.identification))
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

            if (internalType == null || id == null) return null

            var setting: YamlConfiguration? = null
            when (internalType) {
                "item" -> setting = cfg.itemConfig.getItemByID(id)
                "unid" -> setting = cfg.itemConfig.getUnidentifiedByID(id)
            }
            if (setting == null) return null
            return ItemInstance(ItemSetting(setting), ItemStats(identification, cfg), internalType, UUID.randomUUID())
        }
    }

    override val type: Type = ItemInstance::class.java
    override val typeAdapter = ItemInstanceAdapter(cfg)
}