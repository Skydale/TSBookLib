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
        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                List<ItemStack> items = packet.getItemListModifier().read(0);

                for (ItemStack item : items) {
                    updateItem(item);
                }
            }
        });

        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);

                updateItem(item);
            }
        });

        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Client.SET_CREATIVE_SLOT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);

                if (ItemUtils.hasItemID(item)) {
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