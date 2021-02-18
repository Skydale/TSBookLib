package io.github.mg138.tsbook.utils.config.gui.armor;

import org.bukkit.configuration.ConfigurationSection;

public class ArmorSetting {
    public final String type;

    public ArmorSetting(ConfigurationSection section) {
        this.type = section.getString("TYPE");
    }
}
