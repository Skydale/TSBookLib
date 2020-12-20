package io.github.twilight_book.listener.event;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityClick implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public boolean onEntityClick(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return false;

        String s = ItemUtils.getDataTag(Book.getInst(), item, "item");
        if (s == null) return false;

        switch (s) {
            case "LASSO":
                return LASSO(event);
        }
        return false;
    }

    public boolean LASSO(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        entity.addPassenger(event.getPlayer());
        return true;
    }
}