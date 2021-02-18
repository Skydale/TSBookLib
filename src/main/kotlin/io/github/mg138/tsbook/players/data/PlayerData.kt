package io.github.mg138.tsbook.players.data

import dev.reactant.reactant.core.component.Component

@Component
class PlayerData {
    var items = ItemInstanceMapWrapper()
    var normalItems = ItemStackMapWrapper()
    var equipment = ItemInstanceMapWrapper()
}