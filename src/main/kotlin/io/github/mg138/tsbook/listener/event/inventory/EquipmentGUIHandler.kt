package io.github.mg138.tsbook.listener.event.inventory

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.ItemUtils
import io.github.mg138.tsbook.listener.event.inventory.error.ItemError
import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.gui.armor.element.ArmorElementSetting
import io.github.mg138.tsbook.setting.gui.element.GUIElementSetting
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

// TODO: CLEAN UP

object EquipmentGUIHandler : Listener {
    private val players: MutableSet<Player> = HashSet()

    fun unload() {
        players.forEach {
            it.closeInventory()
        }
        players.clear()
    }

    private fun defaultItem(setting: GUIElementSetting): ItemStack {
        val item = ItemStack(setting.material, setting.count)
        val meta = item.itemMeta

        meta?.setCustomModelData(setting.model)
        meta?.setDisplayName(setting.name)
        meta?.lore = setting.lore

        item.itemMeta = meta
        return item
    }

    private fun constructInventory(inventory: Inventory, player: Player) {
        val data = ArcticGlobalDataService.inst.getData<PlayerData>(player) ?: return

        ArmorGUIConfig.forEach { (i, setting) ->
            val item = when {
                setting is ArmorElementSetting && data.equipment.contains(i) -> {
                    data.equipment[i]!!.createItem()
                }
                else -> {
                    defaultItem(setting)
                }
            }
            inventory.setItem(i, item)
        }
    }

    fun openEquipmentGUI(player: Player) {
        val inventory = Bukkit.createInventory(player, 54, BookConfig.translate.get("gui.equipment.name"))
        constructInventory(inventory, player)
        players.add(player)
        player.openInventory(inventory)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player
        if (player !is Player) return

        players.remove(player)
        val runnable = object : BukkitRunnable() {
            override fun run() {
                player.updateInventory()
            }
        }
        runnable.runTaskLater(Book.inst, 1)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked
        if (player !is Player) return

        if (players.contains(player)) {
            val slots = event.rawSlots

            slots.forEach { slot -> if (slot < 54) event.isCancelled = true }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player !is Player) return

        if (players.contains(player)) {
            val slot = event.rawSlot
            val inventory = event.inventory
            val playerInventory = player.inventory
            val currentItem = event.currentItem ?: return
            val cursor = event.cursor ?: return

            when (event.action) {
                InventoryAction.MOVE_TO_OTHER_INVENTORY -> {
                    event.isCancelled = true

                    if (slot in 0..53) {
                        if (ItemUtils.hasItemID(currentItem)) {
                            val setting = ArmorGUIConfig[slot] ?: return
                            if (setting !is ArmorElementSetting) return

                            val failed = playerInventory.addItem(currentItem)
                            if (failed.isNotEmpty()) return
                            ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                data.equipment.remove(slot)
                            }
                            event.currentItem = defaultItem(setting)
                        }
                    } else {
                        if (ItemUtils.hasItemID(currentItem)) {
                            val instance = ItemUtils.getInstByItem(Book.inst, currentItem) ?: return

                            val i = ArmorGUIConfig.getByType(instance.itemType, player)
                            if (i == -1) return

                            inventory.setItem(i, currentItem)
                            ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                data.equipment[i] = instance
                            }
                            event.currentItem = null
                        }
                    }
                }

                InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_SOME -> {
                    if (slot in 0..53) {
                        event.isCancelled = true
                        if (ItemUtils.hasItemID(currentItem)) {
                            player.setItemOnCursor(currentItem)
                            ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                data.equipment.remove(slot)
                            }
                            event.currentItem = defaultItem(ArmorGUIConfig[slot]!!)
                        }
                    }
                }

                InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME -> {
                    if (slot in 0..53) {
                        event.isCancelled = true
                        if (ItemUtils.hasItemID(cursor)) {
                            val setting = ArmorGUIConfig[slot] ?: return
                            if (setting !is ArmorElementSetting) return

                            val instance = ItemUtils.getInstByItem(Book.inst, cursor) ?: return

                            if (setting.setting.type == instance.itemType) {
                                event.currentItem = cursor
                                ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                    data.equipment[slot] = instance
                                }
                                player.setItemOnCursor(null)
                                return
                            }
                        }
                        ItemError.badItem(player)
                    }
                }

                InventoryAction.SWAP_WITH_CURSOR -> {
                    if (slot in 0..53) {
                        event.isCancelled = true

                        if (!ItemUtils.checkItem(currentItem)) return

                        if (ItemUtils.hasItemID(cursor)) {
                            val setting = ArmorGUIConfig[slot] ?: return
                            if (setting !is ArmorElementSetting) return

                            val instance = ItemUtils.getInstByItem(Book.inst, cursor) ?: return

                            if (setting.setting.type == instance.itemType) {
                                event.currentItem = cursor
                                ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                    data.equipment[slot] = instance
                                }
                                player.setItemOnCursor(if (ItemUtils.hasItemID(currentItem)) currentItem else null)
                                return
                            }
                        }
                        ItemError.badItem(player)
                    }
                }

                InventoryAction.HOTBAR_MOVE_AND_READD -> {
                    event.isCancelled = true

                    val hotbarButton = event.hotbarButton
                    if (hotbarButton < 0) return
                    val hotbar = playerInventory.getItem(hotbarButton) ?: return

                    if (ItemUtils.hasItemID(hotbar)) {
                        val setting = ArmorGUIConfig[slot] ?: return
                        if (setting !is ArmorElementSetting) return

                        val instance = ItemUtils.getInstByItem(Book.inst, hotbar) ?: return

                        if (setting.setting.type == instance.itemType) {
                            playerInventory.setItem(
                                hotbarButton,
                                if (ItemUtils.hasItemID(currentItem)) currentItem else null
                            )

                            ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                data.equipment[slot] = instance
                            }
                            inventory.setItem(slot, hotbar)
                            return
                        }
                    }
                    ItemError.badItem(player)
                }

                InventoryAction.HOTBAR_SWAP -> {
                    event.isCancelled = true

                    val setting = ArmorGUIConfig[slot] ?: return
                    val hotbarButton = event.hotbarButton
                    if (hotbarButton < 0) return
                    val hotbar = playerInventory.getItem(hotbarButton) ?: return

                    if (ItemUtils.hasItemID(currentItem)) {
                        playerInventory.setItem(hotbarButton, currentItem)
                        ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                            data.equipment.remove(slot)
                        }
                        inventory.setItem(slot, defaultItem(setting))
                    } else if (ItemUtils.hasItemID(hotbar)) {
                        if (setting !is ArmorElementSetting) return
                        val instance = ItemUtils.getInstByItem(Book.inst, hotbar) ?: return

                        if (setting.setting.type == instance.itemType) {
                            playerInventory.setItem(hotbarButton, null)
                            ArcticGlobalDataService.inst.edit<PlayerData>(player) { data ->
                                data.equipment[slot] = instance
                            }
                            inventory.setItem(slot, hotbar)
                        } else {
                            ItemError.badItem(player)
                        }
                    }
                }

                InventoryAction.NOTHING, InventoryAction.UNKNOWN, InventoryAction.CLONE_STACK,
                InventoryAction.COLLECT_TO_CURSOR -> Unit

                InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ONE_CURSOR,
                InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT -> {
                    event.isCancelled = true
                }
            }

        }
    }
}