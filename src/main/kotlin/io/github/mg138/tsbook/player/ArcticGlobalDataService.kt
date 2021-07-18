package io.github.mg138.tsbook.player

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.item.api.ItemManager
import io.github.mg138.tsbook.player.data.PlayerData
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import tech.clayclaw.arcticglobal.event.PlayerDataUpdateEvent
import tech.clayclaw.arcticglobal.service.PlayerDataService

@Component
class ArcticGlobalDataService(
    private val dataService: PlayerDataService,
    private val eventService: EventService,
    private val freezePlayer: FreezePlayer,
    private val itemManager: ItemManager
) : LifeCycleHook {
    override fun onEnable() {
        dataService.register("tsbooklib", PlayerData::class) {
            PlayerData()
        }

        eventService {
            PlayerDataUpdateEvent::class.observable().subscribe {
                onDataUpdate(it)
            }

            PlayerQuitEvent::class.observable().subscribe {
                onPlayerQuit(it)
            }
        }
    }

    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val inventory = player.inventory

        dataService.edit<PlayerData>(player) { data ->
            for (i in 0 until inventory.size) {
                if (i in 36..40) continue
                if (i == 8) continue

                val itemStack = inventory.getItem(i)

                if (itemStack == null) {
                    data.normalItems.remove(i)
                    data.items.remove(i)
                    continue
                }

                val item = itemManager.get(itemStack)

                when {
                    item != null -> {
                        data.items[i] = item
                        data.normalItems.remove(i)
                    }
                    else -> {
                        data.items.remove(i)
                        data.normalItems[i] = itemStack
                    }
                }
            }
        }

        inventory.clear()
    }

    fun onDataUpdate(event: PlayerDataUpdateEvent) {
        val player = Book.inst.server.getPlayer(event.uuid) ?: return
        val data = dataService.getData<PlayerData>(player) ?: return

        val inventory = player.inventory

        inventory.clear()
        for (i in (0 until inventory.size)) {
            val item = getItemAt(data, i) ?: continue
            inventory.setItem(i, item)
        }

        freezePlayer.remove(player)
    }

    fun getItemAt(data: PlayerData, i: Int): ItemStack? {
        data.items[i]?.let {
            return itemManager.createItemStack(it)
        }

        return data.normalItems[i]
    }
}