package io.github.mg138.tsbook.players.data

import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.items.ItemInstance
import org.bukkit.inventory.ItemStack
import java.util.*

@Component
class PlayerData {
    var items = Vector<ItemInstance>()
    var normalItems = ItemStackVectorWrapper()
    var equipment = Vector<ItemInstance>()
}