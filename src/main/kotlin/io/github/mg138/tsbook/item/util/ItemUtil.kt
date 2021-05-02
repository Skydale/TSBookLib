package io.github.mg138.tsbook.item.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.attribute.stat.identified.data.IdentifiedStat
import io.github.mg138.tsbook.item.attribute.stat.identified.data.Identification
import io.github.mg138.tsbook.item.ItemBase
import io.github.mg138.tsbook.item.data.IdentificationTag
import io.github.mg138.tsbook.item.data.UUIDTag
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

object ItemUtil {
    val ITEM_CACHE: MutableMap<UUID, ItemBase> = HashMap()

    // Non-persistent
    val CACHE_KEY = NamespacedKey(Book.inst, "cache")
    val UUID_ARRAY_KEY = NamespacedKey(Book.inst, "item_uuids")

    // Persistent
    val ID_KEY = NamespacedKey(Book.inst, "id")
    val IDEN_KEY = NamespacedKey(Book.inst, "iden")

    inline fun checkItem(item: ItemStack?, actionOnFail: () -> Unit = {}): Boolean {
        if (item == null || item.type == Material.AIR) {
            actionOnFail()
            return false
        }
        return true
    }

    fun createItem(inst: ItemBase): ItemStack {
        val item = ItemStack(inst.material)
        val meta = item.itemMeta!!
        val uuid = inst.uuid

        meta.setCustomModelData(inst.model)
        setUUID(meta, uuid)
        cacheItem(inst, uuid)
        item.itemMeta = meta
        return item
    }

    fun getInstByItem(item: ItemStack): ItemBase? {
        if (!checkItem(item)) return null
        val meta = item.itemMeta!!

        val uuid = getUUID(meta)?.let {
            return ITEM_CACHE[it]
        } ?: UUID.randomUUID()

        val id = getItemId(meta) ?: return null

        val inst = ItemBase(
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
        meta.persistentDataContainer.get(CACHE_KEY, UUIDTag)

    fun setUUID(meta: ItemMeta, uuid: UUID) =
        meta.persistentDataContainer.set(CACHE_KEY, UUIDTag, uuid)

    //fun hasItemID(meta: ItemMeta) =
    //    hasStringTag(meta, itemKey)

    fun getItemId(meta: ItemMeta) = getStringTag(meta, ID_KEY)

    private fun setIdentification(meta: ItemMeta, identification: Identification) =
        meta.persistentDataContainer.set(IDEN_KEY, IdentificationTag, identification)

    private fun getIdentification(meta: ItemMeta) =
        meta.persistentDataContainer.get(IDEN_KEY, IdentificationTag)

    fun cacheItem(inst: ItemBase, uuid: UUID) {
        ITEM_CACHE[uuid] = inst
    }

    fun unload() {
        ITEM_CACHE.clear()
    }
}