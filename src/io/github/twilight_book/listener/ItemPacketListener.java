package io.github.twilight_book.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemPacketListener {
    public static void register() {
        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                List<ItemStack> items = packet.getItemListModifier().read(0);

                for (ItemStack item : items) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta == null) continue;

                    String ID = ItemUtils.getItemID(item);
                    if (ID == null) continue;

                    ItemInstance inst = ItemUtils.getItem(Book.getInst(), item, ID);
                    if (inst == null) continue;

                    meta.setLore(inst.getLore());
                    meta.setDisplayName(inst.getName());
                    item.setItemMeta(meta);
                }
            }
        });


        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);

                ItemMeta meta = item.getItemMeta();
                if (meta == null) return;

                String ID = ItemUtils.getItemID(item);
                if (ID == null) return;

                ItemInstance inst = ItemUtils.getItem(Book.getInst(), item, ID);
                if (inst == null) return;

                meta.setLore(inst.getLore());
                meta.setDisplayName(inst.getName());
                item.setItemMeta(meta);
            }
        });
        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Client.SET_CREATIVE_SLOT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);

                ItemMeta meta = item.getItemMeta();
                if (meta == null) return;

                meta.setDisplayName(null);
                meta.setLore(null);
                item.setItemMeta(meta);
            }
        });
    }
}