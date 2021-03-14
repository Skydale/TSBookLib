package io.github.mg138.tsbook.setting.gui.element;

import org.bukkit.Material;

import java.util.List;

public class ArmorGUIElementSetting extends GUIElementSetting {
    public final ArmorElementSetting setting;

    public ArmorGUIElementSetting(int slot, Material material, int count, String name, List<String> lore, int model, ArmorElementSetting setting) {
        super(slot, material, count, name, lore, model);
        this.setting = setting;
    }
}
