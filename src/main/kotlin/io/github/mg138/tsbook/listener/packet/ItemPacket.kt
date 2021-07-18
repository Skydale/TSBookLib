package io.github.mg138.tsbook.listener.packet

import com.comphenix.packetwrapper.WrapperPlayClientSetCreativeSlot
import com.comphenix.packetwrapper.WrapperPlayServerSetSlot
import com.comphenix.packetwrapper.WrapperPlayServerWindowItems
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.github.mg138.tsbook.item.api.ItemManager
import io.github.mg138.tsbook.item.util.ItemUtil
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

object ItemPacket : io.github.mg138.tsbook.event.PacketListener {
    private fun ItemStack.updateItem() {
        val meta = this.itemMeta ?: return
        val item = ItemManager.get(this) ?: return

        meta.lore = item.getLore()
        meta.setDisplayName(item.getName())

        this.itemMeta = meta
    }

    override fun getPacketAdapters(plugin: JavaPlugin): Iterable<PacketAdapter> {
        return listOf(
            object : PacketAdapter(plugin, ListenerPriority.NORMAL, WrapperPlayServerWindowItems.TYPE) {
                override fun onPacketSending(event: PacketEvent) {
                    WrapperPlayServerWindowItems(event.packet).slotData.forEach { it.updateItem() }
                }
            },
            object : PacketAdapter(plugin, ListenerPriority.NORMAL, WrapperPlayServerSetSlot.TYPE) {
                override fun onPacketSending(event: PacketEvent) {
                    WrapperPlayServerSetSlot(event.packet).slotData.updateItem()
                }
            },
            object : PacketAdapter(plugin, ListenerPriority.NORMAL, WrapperPlayServerSetSlot.TYPE) {
                override fun onPacketReceiving(event: PacketEvent)  {
                    val item = WrapperPlayClientSetCreativeSlot(event.packet).clickedItem
                    val meta = item.itemMeta ?: return

                    if (ItemUtil.hasUUID(meta)) {
                        meta.setDisplayName(null)
                        meta.lore = null
                        item.itemMeta = meta
                    }
                }
            }
        )
    }
}