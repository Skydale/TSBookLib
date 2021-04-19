package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.data.IdentifiedStat
import io.github.mg138.tsbook.item.data.Identification
import io.github.mg138.tsbook.item.ItemInstance
import io.github.mg138.tsbook.item.storage.IdentificationTag
import io.github.mg138.tsbook.item.storage.UUIDTag
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

object ItemUtil {
    val ITEM_CACHE: MutableMap<UUID, ItemInstance> = HashMap()

    // Non-persistent
    val cacheKey = NamespacedKey(Book.inst, "cache")
    val uuidArrayKey = NamespacedKey(Book.inst, "item_uuids")

    // Persistent
    val idKey = NamespacedKey(Book.inst, "id")
    val identificationKey = NamespacedKey(Book.inst, "iden")

    inline fun checkItem(item: ItemStack?, actionOnFail: () -> Unit = {}): Boolean {
        if (item == null || item.type == Material.AIR) {
            actionOnFail()
            return false
        }
        return true
    }

    fun createItem(inst: ItemInstance): ItemStack {
        val item = ItemStack(inst.material)
        val meta = item.itemMeta!!
        val uuid = inst.uuid

        meta.setCustomModelData(inst.model)
        setUUID(meta, uuid)
        cacheItem(inst, uuid)
        item.itemMeta = meta
        return item
    }

    fun getInstByItem(item: ItemStack): ItemInstance? {
        if (!checkItem(item)) return null
        val meta = item.itemMeta!!

        val uuid = getUUID(meta)?.let {
            return ITEM_CACHE[it]
        } ?: UUID.randomUUID()

        val id = getItemId(meta) ?: return null

        val inst = ItemInstance(
            id,
            IdentifiedStat.create(
                id,
                getIdentification(meta)
            ),
            uuid
        )
        ITEM_CACHE[uuid] = inst
        return inst
    }



    //fun hasStringTag(meta: ItemMeta, key: NamespacedKey) =
    //    meta.persistentDataContainer.has(key, PersistentDataType.STRING)

    fun getStringTag(meta: ItemMeta, key: NamespacedKey) =
        meta.persistentDataContainer.get(key, PersistentDataType.STRING)

    //fun setStringTag(meta: ItemMeta, key: NamespacedKey, value: String) =
    //    meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)

    //fun hasStringTag(meta: ItemMeta, key: String) =
    //    hasStringTag(meta, NamespacedKey(Book.inst, key))

    fun getStringTag(meta: ItemMeta, key: String) =
        getStringTag(meta, NamespacedKey(Book.inst, key))

    //fun setStringTag(meta: ItemMeta, key: String, value: String) =
    //    setStringTag(meta, NamespacedKey(Book.inst, key), value)



    fun getUUID(meta: ItemMeta) =
        meta.persistentDataContainer.get(cacheKey, UUIDTag)

    fun setUUID(meta: ItemMeta, uuid: UUID) =
        meta.persistentDataContainer.set(cacheKey, UUIDTag, uuid)

    //fun hasItemID(meta: ItemMeta) =
    //    hasStringTag(meta, itemKey)

    fun getItemId(meta: ItemMeta) = getStringTag(meta, idKey)

    private fun setIdentification(meta: ItemMeta, identification: Identification) =
        meta.persistentDataContainer.set(identificationKey, IdentificationTag, identification)

    private fun getIdentification(meta: ItemMeta) =
        meta.persistentDataContainer.get(identificationKey, IdentificationTag)

    fun cacheItem(inst: ItemInstance, uuid: UUID) {
        ITEM_CACHE[uuid] = inst
    }

    fun unload() {
        ITEM_CACHE.clear()
    }
}