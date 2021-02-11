package io.github.mg138.tsbook.players

import com.comphenix.packetwrapper.WrapperPlayServerLogin
import com.comphenix.packetwrapper.WrapperPlayServerPosition
import com.comphenix.protocol.wrappers.EnumWrappers
import dev.reactant.reactant.core.component.Component
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook
import dev.reactant.reactant.service.spec.server.EventService
import io.github.mg138.tsbook.Book
import org.bukkit.event.player.PlayerQuitEvent
import io.github.mg138.tsbook.items.ItemUtils
import io.github.mg138.tsbook.players.data.PlayerData
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import tech.clayclaw.arcticglobal.event.PlayerDataUpdateEvent
import tech.clayclaw.arcticglobal.service.PlayerDataService

@Component
class ArcticPlayerDataService(
    private val dataService: PlayerDataService,
    private val eventService: EventService,
    private val freezePlayer: FreezePlayer
) : LifeCycleHook {

    override fun onEnable() {
        dataService.register("tsbooklib", PlayerData::class) { PlayerData() }

        eventService.registerBy(this) {
            PlayerJoinEvent::class.observable()
                .subscribe {
                    val player = it.player
                    freezePlayer.frozen.add(player)
                    val packet = WrapperPlayServerLogin()
                    packet.entityID = player.entityId
                    packet.gamemode = EnumWrappers.NativeGameMode.fromBukkit(player.gameMode)
                    packet.reducedDebugInfo = player.world.getGameRuleValue(GameRule.REDUCED_DEBUG_INFO) == true
                    packet.resourceKey = player.world
                    packet.worlds = player.server.worlds.toSet()
                    packet.dimension = player.world.environment.ordinal
                    packet.broadcastPacket()
                }

            PlayerDataUpdateEvent::class.observable()
                .subscribe {
                    val player = Book.inst.server.getPlayer(it.uuid)!!
                    val data = dataService.getData<PlayerData>(player) ?: return@subscribe
                    val inventory = player.inventory
                    inventory.clear()
                    for (i in 0 until inventory.size) {
                        try {
                            val itemInstance = data.items[i]
                            if (itemInstance == null) {
                                val itemStack = data.normalItems[i] ?: continue
                                inventory.setItem(i, itemStack)
                                continue
                            }
                            inventory.setItem(i, itemInstance.createItem(Book.inst))
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            break
                        }
                    }
                    freezePlayer.frozen.remove(player)
                    //val packet = WrapperPlayServerPosition()
                    //packet.sendPacket(player)
                }

            PlayerQuitEvent::class.observable()
                .subscribe {
                    val player = it.player
                    val inventory = player.inventory
                    for (i in 0 until inventory.size) {
                        val item = inventory.getItem(i) ?: ItemStack(Material.AIR)
                        if (ItemUtils.hasItemID(item)) {
                            dataService.edit(player) { data: PlayerData ->
                                data.items.setSize(i + 1)
                                data.items[i] = ItemUtils.getInstByItem(Book.inst, item)
                                data.normalItems.setSize(i + 1)
                                data.normalItems[i] = null
                            }
                        } else {
                            dataService.edit(player) { data: PlayerData ->
                                data.normalItems.setSize(i + 1)
                                data.normalItems[i] = item
                                data.items.setSize(i + 1)
                                data.items[i] = null
                            }
                        }
                    }
                    inventory.clear()
                }
        }
    }
}