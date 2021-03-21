package io.github.mg138.tsbook.players

import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.players.data.PlayerData
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import tech.clayclaw.arcticglobal.event.PlayerDataUpdateEvent
import tech.clayclaw.arcticglobal.service.PlayerDataService

@Component
class ArcticGlobalDataService(
    private val dataService: PlayerDataService,
    private val eventService: EventService,
    private val freezePlayer: FreezePlayer
) : LifeCycleHook {
    companion object {
        lateinit var inst: PlayerDataService
        val playerDataRef = PlayerData::class
    }

    override fun onEnable() {
        inst = dataService

        dataService.register("tsbooklib", playerDataRef) { PlayerData() }

        eventService.registerBy(this) {
            PlayerDataUpdateEvent::class.observable()
                .subscribe {
                    val player = Book.inst.server.getPlayer(it.uuid)!!
                    val data = dataService.getData<PlayerData>(player) ?: return@subscribe
                    val inventory = player.inventory
                    inventory.clear()

                    for (i in 0 until inventory.size) {
                        val item: ItemStack

                        val itemInstance = data.items[i]

                        item = itemInstance?.createItem() ?: (data.normalItems[i] ?: continue)
                        inventory.setItem(i, item)
                    }

                    freezePlayer.remove(player)
                }

            PlayerQuitEvent::class.observable()
                .subscribe {
                    val player = it.player
                    val inventory = player.inventory

                    dataService.edit(player) { data: PlayerData ->
                        for (i in 0 until inventory.size) {
                            if (i in 36..40) continue
                            if (i == 8) continue
                            val item = inventory.getItem(i)

                            when {
                                item == null -> {
                                    data.normalItems.remove(i)
                                    data.items.remove(i)
                                }

                                ItemUtils.hasItemID(item) -> {
                                    data.normalItems.remove(i)
                                    ItemUtils.getInstByItem(Book.inst, item)?.let { data.items[i] = it }
                                }

                                else -> {
                                    data.items.remove(i)
                                    data.normalItems[i] = item
                                }
                            }
                        }
                    }
                    inventory.clear()
                }
        }
    }
}