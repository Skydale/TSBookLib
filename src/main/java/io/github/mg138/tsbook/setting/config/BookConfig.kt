package io.github.mg138.tsbook.setting.config

import org.bukkit.configuration.file.YamlConfiguration

class BookConfig(val locale: String) {
    constructor(setting: YamlConfiguration) : this(
        setting.getString("locale")!!
    )
}