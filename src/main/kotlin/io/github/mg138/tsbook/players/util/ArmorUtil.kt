package io.github.mg138.tsbook.players.util

import io.github.mg138.tsbook.players.ArcticGlobalDataService
import io.github.mg138.tsbook.players.data.PlayerData
import io.github.mg138.tsbook.utils.config.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.utils.config.gui.armor.ArmorGUIElementSetting
import org.bukkit.entity.Player

object ArmorUtil {
    fun getByType(type: String, player: Player, armorConfig: ArmorGUIConfig): Int {
        val data = ArcticGlobalDataService.dataServiceInstance.getData<PlayerData>(player) ?: return -1

        armorConfig.elementSettings.forEach { (i, setting) ->
            if (!data.equipment.containsKey(i)) {
                if (setting is ArmorGUIElementSetting) {
                    if (setting.setting.type == type) return i
                }
            }
        }

        return -1
    }
}