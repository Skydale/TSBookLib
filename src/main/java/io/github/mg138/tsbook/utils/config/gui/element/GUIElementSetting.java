package io.github.mg138.tsbook.utils.config.gui.element;

import org.bukkit.Material;

import java.util.List;

public class GUIElementSetting {
    public final int slot;
    public final Material material;
    public final int count;
    public final String name;
    public final List<String> lore;
    public final int model;

    public GUIElementSetting(int slot, Material material, int count, String name, List<String> lore, int model) {
        this.slot = slot;
        this.material = material;
        this.count = count;
        this.name = name;
        this.lore = lore;
        this.model = model;
    }
}
