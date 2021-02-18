package io.github.mg138.tsbook.utils.config.gui.armor;

import io.github.mg138.tsbook.utils.config.gui.GUIElementSetting;
import org.bukkit.Material;

import java.util.List;

public class ArmorGUIElementSetting extends GUIElementSetting {
    public final ArmorSetting setting;

    public ArmorGUIElementSetting(int slot, Material material, int count, String name, List<String> lore, int model, ArmorSetting setting) {
        super(slot, material, count, name, lore, model);
        this.setting = setting;
    }
}
