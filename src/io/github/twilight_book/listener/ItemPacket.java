package io.github.twilight_book.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class ItemPacket {
    private static void update(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String ID = ItemUtils.getItemID(item);
        if (ID == null) return;

        ItemInstance inst = ItemUtils.getItem(Book.getInst(), item, ID);
        if (inst == null) return;

        String matSetting = inst.getConfig().getString("MATERIAL");
        if (matSetting == null) throw new IllegalArgumentException("Cannot get Material of the item.");

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
                    update(item);
                }
            }
        });

        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack item = packet.getItemModifier().read(0);
                update(item);
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