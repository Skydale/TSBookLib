package io.github.mg138.tsbook.listener.packet

import com.comphenix.packetwrapper.WrapperPlayClientSetCreativeSlot
import com.comphenix.packetwrapper.WrapperPlayServerSetSlot
import com.comphenix.packetwrapper.WrapperPlayServerWindowItems
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.ItemUtils.checkItem
import io.github.mg138.tsbook.item.ItemUtils.getInstByItem
import io.github.mg138.tsbook.item.ItemUtils.getInternalItemType
import org.bukkit.inventory.ItemStack

object ItemPacket {
    fun updateItem(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val inst = getInstByItem(Book.inst, item) ?: return
        meta.lore = inst.lore
        meta.setDisplayName(inst.name)
        item.itemMeta = meta
    }

    fun register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(Book.inst, ListenerPriority.HIGH, WrapperPlayServerWindowItems.TYPE) {
                override fun onPacketSending(event: PacketEvent) {
                    for (item in WrapperPlayServerWindowItems(event.packet).slotData) {
                        updateItem(item)
                    }
                }
            }
        )
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(Book.inst, ListenerPriority.HIGH, WrapperPlayServerSetSlot.TYPE) {
                override fun onPacketSending(event: PacketEvent) {
                    updateItem(WrapperPlayServerSetSlot(event.packet).slotData)
                }
            }
        )
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(Book.inst, ListenerPriority.HIGH, WrapperPlayClientSetCreativeSlot.TYPE) {
                override fun onPacketReceiving(event: PacketEvent) {
                    val item = WrapperPlayClientSetCreativeSlot(event.packet).clickedItem
                    if (!checkItem(item)) return
                    val type = getInternalItemType(item) ?: return
                    when (type) {
                        "item", "unid" -> {
                            val meta = item.itemMeta ?: return
                            meta.setDisplayName(null)
                            meta.lore = null
                            item.itemMeta = meta
                        }
                    }
                }
            }
        )
    }
}