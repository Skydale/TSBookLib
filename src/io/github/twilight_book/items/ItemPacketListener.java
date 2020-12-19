package io.github.twilight_book.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import io.github.twilight_book.Book;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemPacketListener {
    public static void register() {
        /*Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                List<ItemStack> a = packet.getItemListModifier().read(0);
                List<ItemStack> c = new ArrayList<>();

                for(ItemStack b : a){
                    if(b!=null && b.getType() == Material.EGG) {
                        ItemMeta eggmeta = b.getItemMeta();
                        eggmeta.setLore(Arrays.asList("hi","bye"));
                        b.setItemMeta(eggmeta);
                        event.getPlayer().sendMessage("EGG");
                    }
                    c.add(b);
                }

                packet.getItemListModifier().write(0,c);

                event.setPacket(packet);



                /*for(ItemStack b : a){
                    ItemInstance.getFromExist(b);
                }
            }
        });*/
        Book.getInst().getProtocolManager().addPacketListener(new PacketAdapter(Book.getInst(), ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                ItemStack a = packet.getItemModifier().read(0);

                ItemInstance i = ItemInstance.getFromExist(a);
                if (i == null) return;
                ItemMeta meta = a.getItemMeta();
                meta.setLore(i.getLore());
                meta.setDisplayName(i.getDisplayName());
                a.setItemMeta(meta);

            }
        });
    }
}