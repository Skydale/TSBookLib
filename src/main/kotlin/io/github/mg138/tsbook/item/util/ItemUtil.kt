package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.data.adapter.bukkit.UUIDTag
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta

object ItemUtil {
    /*
    fun hasStringTag(meta: ItemMeta, key: NamespacedKey) =
        meta.persistentDataContainer.has(key, PersistentDataType.STRING)

    fun getStringTag(meta: ItemMeta, key: NamespacedKey) =
        meta.persistentDataContainer.get(key, PersistentDataType.STRING)

    //fun setStringTag(meta: ItemMeta, key: NamespacedKey, value: String) =
    //    meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)

    fun hasStringTag(meta: ItemMeta, key: String) =
        hasStringTag(meta, NamespacedKey(Book.inst, key))

    fun getStringTag(meta: ItemMeta, key: String) =
        getStringTag(meta, NamespacedKey(Book.inst, key))

    //fun setStringTag(meta: ItemMeta, key: String, value: String) =
    //    setStringTag(meta, NamespacedKey(Book.inst, key), value)
    */


    // # Non-persistent

    // UUID
    private val UUID_KEY = NamespacedKey(Book.inst, "uuid")

    fun getUUID(meta: ItemMeta) =
        meta.persistentDataContainer.get(UUID_KEY, UUIDTag)

    fun hasUUID(meta: ItemMeta) =
        meta.persistentDataContainer.has(UUID_KEY, UUIDTag)

    fun setUUID(meta: ItemMeta, item: ItemBase) =
        meta.persistentDataContainer.set(UUID_KEY, UUIDTag, item.getUUID())
    // UUID

    // UUID Array
    private val UUID_ARRAY_KEY = NamespacedKey(Book.inst, "item_uuids")
    // UUID Array



    /*
    // # Persistent

    // Id
    private val ID_KEY = NamespacedKey(Book.inst, "id")

    fun getIdentifier(meta: ItemMeta) = getStringTag(meta, ID_KEY)?.let { Identifier.of(it) }

    fun hasIdentifier(meta: ItemMeta) = hasStringTag(meta, ID_KEY)
    // Id

    // Data
    private val DATA_KEY = NamespacedKey(Book.inst, "data")

    fun setDataMap(meta: ItemMeta, dataMap: DataMap) =
        meta.persistentDataContainer.set(DATA_KEY, DataMapTag, dataMap)

    fun getDataMap(meta: ItemMeta) =
        meta.persistentDataContainer.get(DATA_KEY, DataMapTag)
    // Data

    fun setIdentification(meta: ItemMeta, identification: Identification) {
        val dataMap = this.getDataMap(meta) ?: DataMap()
        dataMap += identification
        this.setDataMap(meta, dataMap)
    }
     */
}