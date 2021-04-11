package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.attribute.InternalItemType
import io.github.mg138.tsbook.item.data.IdentificationTag
import io.github.mg138.tsbook.item.data.UUIDTag
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object ItemUtils {
    val itemCache: MutableMap<UUID, ItemInstance> = HashMap()

    val itemKey = NamespacedKey(Book.inst, "item")
    val uuidKey = NamespacedKey(Book.inst, "uuid")
    val internalItemTypeKey = NamespacedKey(Book.inst, "internal")
    val itemTypeKey = NamespacedKey(Book.inst, "type")
    val identificationKey = NamespacedKey(Book.inst, "iden")
    val uuidArrayKey = NamespacedKey(Book.inst, "item_uuids")

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

        val container = meta.persistentDataContainer

        container[uuidKey, UUIDTag] = uuid
        itemCache[uuid] = inst

        val internalItemType = inst.internalItemType
        container[internalItemTypeKey, PersistentDataType.STRING] = internalItemType.string
        container[NamespacedKey(Book.inst, internalItemType.string), PersistentDataType.STRING] = inst.id

        container[itemTypeKey, PersistentDataType.STRING] = inst.itemType.name
        meta.setCustomModelData(inst.model)

        item.itemMeta = meta
        setIdentification(inst, item)
        return item
    }

    fun getInstByItem(plugin: JavaPlugin, item: ItemStack): ItemInstance? {
        if (!checkItem(item)) return null
        val meta = item.itemMeta!!
        val container = meta.persistentDataContainer

        val uuid = container[uuidKey, UUIDTag].let { uuid ->
            itemCache[uuid]?.let { return it }
            UUID.randomUUID()
        }

        val internalItemType = getInternalItemType(item) ?: return null
        val id = container[NamespacedKey(plugin, internalItemType.string), PersistentDataType.STRING] ?: return null

        val inst = ItemInstance(
            id,
            ItemStat.create(
                getIdentification(item),
                id
            ),
            internalItemType,
            uuid
        )
        itemCache[uuid] = inst
        return inst
    }

    private fun setIdentification(inst: ItemInstance, item: ItemStack) {
        val meta = item.itemMeta ?: return
        val identification = inst.itemStat?.identification ?: return
        meta.persistentDataContainer[identificationKey, IdentificationTag] = identification
        item.itemMeta = meta
    }

    private fun getIdentification(item: ItemStack): ItemIdentification? {
        return item.itemMeta!!.persistentDataContainer[identificationKey, IdentificationTag]
    }

    fun getUUID(item: ItemStack): UUID? {
        return item.itemMeta!!.persistentDataContainer[uuidKey, UUIDTag]
    }

    fun getStringTag(item: ItemStack, key: String): String? {
        return item.itemMeta!!.persistentDataContainer[NamespacedKey(Book.inst, key), PersistentDataType.STRING]
    }

    fun getInternalItemType(item: ItemStack): InternalItemType? {
        val container = item.itemMeta!!.persistentDataContainer

        return container[internalItemTypeKey, PersistentDataType.STRING]?.let { InternalItemType.of(it) }
    }

    fun hasItemID(item: ItemStack): Boolean {
        if (!checkItem(item)) return false
        return item.itemMeta!!.persistentDataContainer.has(itemKey, PersistentDataType.STRING)
    }

    fun unload() {
        itemCache.clear()
    }
}