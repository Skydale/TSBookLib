package io.github.mg138.tsbook.player.data

import dev.reactant.reactant.core.component.Component

@Component
class PlayerData {
    var items = ItemBaseMapWrapper()
    var normalItems = ItemStackMapWrapper()
    var equipment = ItemBaseMapWrapper()
}