package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemInstance;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUpdate implements Listener {
    private void update(ItemStack item) {
        if (item == null) return;

        if (ItemUtils.hasItemID(item)) {
            ItemInstance inst = ItemUtils.getInstByItem(Book.inst, item);
            if (inst == null) throw new NullPointerException("Cannot create item instance.");

            item.setType(inst.getMaterial());

            ItemMeta meta = item.getItemMeta();
            if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

            meta.setCustomModelData(inst.getModel());

            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        update(event.getCurrentItem());
    }
}
