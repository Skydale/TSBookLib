package io.github.mg138.tsbook.listener.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemUtils;
import com.comphenix.packetwrapper.WrapperPlayClientSetCreativeSlot;
import com.comphenix.packetwrapper.WrapperPlayServerSetSlot;
import com.comphenix.packetwrapper.WrapperPlayServerWindowItems;
import io.github.mg138.tsbook.items.ItemInstance;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemPacket {
    public static void updateItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String type = ItemUtils.getItemType(item);
        if (type == null) return;

        ItemInstance inst = ItemUtils.getInstByItem(Book.getInst(), item, type);
        if (inst == null) return;

        meta.setLore(inst.getLore());
        meta.setDisplayName(inst.getName());
        item.setItemMeta(meta);
    }

    public static void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, WrapperPlayServerWindowItems.TYPE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event.getPacket());
                List<ItemStack> items = packet.getSlotData();

                for (ItemStack item : items) {
                    updateItem(item);
                }
            }
        });


        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, WrapperPlayServerSetSlot.TYPE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event.getPacket());

                updateItem(packet.getSlotData());
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, WrapperPlayClientSetCreativeSlot.TYPE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientSetCreativeSlot packet = new WrapperPlayClientSetCreativeSlot(event.getPacket());
                ItemStack item = packet.getClickedItem();

                String type = ItemUtils.getItemType(item);
                if (type == null) return;

                switch (type) {
                    case "item":
                    case "unid":
                        ItemMeta meta = item.getItemMeta();
                        if (meta == null) return;

                        meta.setDisplayName(null);
                        meta.setLore(null);
                        item.setItemMeta(meta);
                }
            }
        });
    }
}