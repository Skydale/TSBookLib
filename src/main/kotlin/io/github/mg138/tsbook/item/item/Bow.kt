package io.github.mg138.tsbook.item.item

import io.github.mg138.tsbook.config.item.element.ItemSetting
import io.github.mg138.tsbook.stat.identified.data.IdentifiedStat
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class Bow(setting: ItemSetting, identifiedStat: IdentifiedStat) : NormalItem(setting, identifiedStat) {
    override fun getItemType() = Companion.getItemType()

    override fun onPlayerInteract(event: PlayerInteractEvent) {
        TODO("Not yet implemented")
    }

    override fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        TODO("Not yet implemented")
    }
}