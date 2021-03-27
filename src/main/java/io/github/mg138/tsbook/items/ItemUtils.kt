package io.github.mg138.tsbook.items

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.data.IdentificationTag
import io.github.mg138.tsbook.items.data.UUIDArrayTag
import io.github.mg138.tsbook.items.data.UUIDTag
import io.github.mg138.tsbook.setting.BookConfig
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object ItemUtils {
    val itemCache: MutableMap<UUID, ItemInstance> = HashMap()

    val uuidArrayTag = UUIDArrayTag()
    val uuidTag = UUIDTag()
    val identificationTag = IdentificationTag()

    val itemKey = NamespacedKey(Book.inst, "item")
    val uuidKey = NamespacedKey(Book.inst, "uuid")
    val internalTypeKey = NamespacedKey(Book.inst, "internal")
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
        val item = ItemStack(inst.material, 1)
        val meta = item.itemMeta!!
        val uuid = inst.uuid

        val container = meta.persistentDataContainer

        container[uuidKey, uuidTag] = uuid
        itemCache[uuid] = inst

        //item type & internal type
        val internalType = inst.internalType
        container[internalTypeKey, PersistentDataType.STRING] = internalType
        container[NamespacedKey(Book.inst, internalType), PersistentDataType.STRING] = inst.id
        //item type & internal type

        container[itemTypeKey, PersistentDataType.STRING] = inst.itemType
        meta.setCustomModelData(inst.model)

        item.itemMeta = meta
        setIdentification(inst, item)
        return item
    }

    fun getInstByItem(plugin: JavaPlugin, item: ItemStack): ItemInstance? {
        if (!checkItem(item)) return null
        val meta = item.itemMeta!!

        val container = meta.persistentDataContainer

        val uuid = container[uuidKey, uuidTag].let { uuid ->
            itemCache[uuid]?.let { return it }
            UUID.randomUUID()
        }

        val internalType = container[internalTypeKey, PersistentDataType.STRING] ?: return null
        val id = container[NamespacedKey(plugin, internalType), PersistentDataType.STRING] ?: return null

        val inst = ItemInstance(
            id,
            ItemStats.create(
                getIdentification(item),
                BookConfig,
                id
            ),
            internalType,
            uuid
        )
        itemCache[uuid] = inst
        return inst
    }

    private fun setIdentification(inst: ItemInstance, item: ItemStack) {
        val meta = item.itemMeta ?: return
        val identification = inst.itemStats?.identification ?: return
        meta.persistentDataContainer[identificationKey, identificationTag] = identification
        item.itemMeta = meta
    }

    private fun getIdentification(item: ItemStack): ItemIdentification? {
        return item.itemMeta!!.persistentDataContainer[identificationKey, identificationTag]
    }

    fun getUUID(item: ItemStack): UUID? {
        return item.itemMeta!!.persistentDataContainer[uuidKey, uuidTag]
    }

    fun getStringTag(item: ItemStack, key: String): String? {
        return item.itemMeta!!.persistentDataContainer[NamespacedKey(Book.inst, key), PersistentDataType.STRING]
    }

    fun getInternalItemType(item: ItemStack): String? {
        val container = item.itemMeta!!.persistentDataContainer

        val internalType = container[internalTypeKey, PersistentDataType.STRING] ?: return null
        return container[NamespacedKey(Book.inst, internalType), PersistentDataType.STRING]
    }

    fun hasItemID(item: ItemStack): Boolean {
        if (!checkItem(item)) return false
        return item.itemMeta!!.persistentDataContainer.has(itemKey, PersistentDataType.STRING)
    }

    fun unload() {
        itemCache.clear()
    }
}