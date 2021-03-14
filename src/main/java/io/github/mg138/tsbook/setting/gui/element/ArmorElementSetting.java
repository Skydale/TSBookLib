package io.github.mg138.tsbook.setting.gui.element;

import org.bukkit.configuration.ConfigurationSection;

public class ArmorElementSetting {
    public final String type;

    public ArmorElementSetting(ConfigurationSection section) {
        this.type = section.getString("TYPE");
    }
}
