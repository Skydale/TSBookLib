package io.github.mg138.tsbook.setting.gui.armor.element

import org.bukkit.configuration.ConfigurationSection

class ArmorSetting(val type: String) {
    constructor(section: ConfigurationSection) : this(
        section.getString("TYPE")!!
    )
}