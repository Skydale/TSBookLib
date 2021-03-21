package io.github.mg138.tsbook.setting.mob

import io.github.mg138.tsbook.setting.mob.element.MobSetting
import org.bukkit.configuration.ConfigurationSection

object MobConfig {
    private val mobSettings: MutableMap<String, MobSetting> = HashMap()

    operator fun get(key: String) = mobSettings[key]

    fun load(config: Map<String, ConfigurationSection>) {
        config.forEach { (id, setting) ->
            mobSettings[id] = MobSetting.create(setting)
        }
    }

    fun unload() {
        mobSettings.clear()
    }
}