package io.github.mg138.tsbook.setting.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class BookConfig {
    public final String locale;

    public BookConfig(YamlConfiguration setting) {
        this(
                setting.getString("locale")
        );
    }

    public BookConfig(String locale) {
        this.locale = locale;
    }
}
