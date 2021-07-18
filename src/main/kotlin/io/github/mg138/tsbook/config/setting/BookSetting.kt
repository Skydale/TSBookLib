package io.github.mg138.tsbook.config.setting

import org.bukkit.configuration.file.YamlConfiguration

class BookSetting(val locale: String) {
    constructor(setting: YamlConfiguration) : this(
        setting.getString("locale")!!
    )
}