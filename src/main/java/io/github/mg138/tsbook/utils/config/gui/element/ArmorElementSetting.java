package io.github.mg138.tsbook.utils.config.gui.element;

import org.bukkit.configuration.ConfigurationSection;

public class ArmorElementSetting {
    public final String type;

    public ArmorElementSetting(ConfigurationSection section) {
        this.type = section.getString("TYPE");
    }
}
