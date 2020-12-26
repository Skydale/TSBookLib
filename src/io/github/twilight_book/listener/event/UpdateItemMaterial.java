package io.github.twilight_book.listener.event;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemInstance;
import io.github.twilight_book.items.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UpdateItemMaterial implements Listener {
    private void update(ItemStack item) {
        if (item == null) return;

        if (ItemUtils.hasItemID(item)) {
            ItemInstance inst = ItemUtils.getInstByItem(Book.getInst(), item, "item");
            if (inst == null) throw new NullPointerException("Cannot get item instance.");

            item.setType(inst.getMaterial());

            ItemMeta meta = item.getItemMeta();
            if (meta == null) throw new NullPointerException("Somehow, I cannot get the metadata of the item.");

            meta.setCustomModelData(inst.getModel() == 0 ? null : inst.getModel());

            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        update(event.getCurrentItem());
    }
}
