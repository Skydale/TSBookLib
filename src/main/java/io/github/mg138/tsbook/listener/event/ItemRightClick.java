package io.github.mg138.tsbook.listener.event;

import io.github.mg138.tsbook.items.ItemUtils;
import io.github.mg138.tsbook.Book;

import org.bukkit.*;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemRightClick implements Listener {
    static final Map<Player, Long> damageCD = new HashMap<>();

    public static void unload() {
        damageCD.clear();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public boolean onEntityClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return false;

        String ID = ItemUtils.getDataTag(Book.getInst(), item, "item");
        if (ID != null) {
            itemIDOperator(ID, event);
        }

        String type = ItemUtils.getDataTag(Book.getInst(), item, "type");
        if (type != null) {
            itemTypeOperator(type, item, player);
        }

        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public boolean onItemClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = event.getItem();
            if (item == null) return false;
            if (item.getType() == Material.AIR) return false;

            String type = ItemUtils.getDataTag(Book.getInst(), item, "type");
            if (type == null) return false;

            itemTypeOperator(type, item, event.getPlayer());
            return true;
        }
        return false;
    }

    public void itemTypeOperator(String type, ItemStack item, Player player) {
        switch (type) {
            case "BOOK":
                BOOK(item, player);
        }
    }

    public void itemIDOperator(String itemID, PlayerInteractEntityEvent event) {
        switch (itemID) {
            case "LASSO":
                LASSO(event);
        }
    }

    public void LASSO(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        entity.addPassenger(event.getPlayer());
    }

    public void BOOK(ItemStack item, Player player) {
        if (damageCD.putIfAbsent(player, System.currentTimeMillis()) != null) {
            if ((System.currentTimeMillis() - damageCD.get(player)) < 500) {
                return;
            }
            damageCD.replace(player, System.currentTimeMillis());
        }
        player.setCooldown(item.getType(), 10);

        UUID uuid = ItemUtils.getUUID(item, Book.getInst());
        if (uuid == null) return;

        Location loc = player.getLocation();

        Arrow arrow = player.getWorld().spawn(player.getEyeLocation(), Arrow.class, aw -> {
            PersistentDataContainer container = aw.getPersistentDataContainer();

            container.set(
                    new NamespacedKey(Book.getInst(), "item_uuid"),
                    ItemUtils.UUID_TAG_TYPE,
                    uuid
            );

            aw.setGravity(false);
            aw.setInvulnerable(true);
            aw.setSilent(true);
            aw.setShooter(player);
            aw.setDamage(0);
            aw.setVelocity(loc.getDirection());
            aw.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        });
        Bukkit.getScheduler().runTaskLater(Book.getInst(), arrow::remove, 120);
    }
}