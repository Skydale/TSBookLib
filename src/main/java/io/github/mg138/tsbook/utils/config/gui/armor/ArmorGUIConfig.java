package io.github.mg138.tsbook.utils.config.gui.armor;

import io.github.mg138.tsbook.utils.Translate;
import io.github.mg138.tsbook.utils.config.gui.GUIElementSetting;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class ArmorGUIConfig {
    private static ArmorGUIConfig instance = null;

    public static ArmorGUIConfig getInstance() {
        if (instance == null) instance = new ArmorGUIConfig();
        return instance;
    }

    private ArmorGUIConfig() {
    }

    public final Map<Integer, GUIElementSetting> elementSettings = new HashMap<>();

    public void load(YamlConfiguration yaml, Translate translate) {
        elementSettings.clear();

        Set<String> keys = yaml.getKeys(false);
        for (String key : keys) {
            int i = Integer.parseInt(key);

            ConfigurationSection section = yaml.getConfigurationSection(key);
            if (section == null)
                throw new NullPointerException("Something wrong happened when iterating through the config!");

            Material material = Material.valueOf(section.getString("MATERIAL"));

            int count = section.getInt("COUNT");

            String name = translate.translate("NAME", null, section);

            List<String> lore = translate.translateList("LORE", null, section);
            lore = lore == null ? Collections.emptyList() : lore;

            int model = section.getInt("MODEL");

            GUIElementSetting elementSetting;
            if (section.contains("EQUIPMENT")) {
                ConfigurationSection equipment = section.getConfigurationSection("EQUIPMENT");
                if (equipment == null)
                    throw new NullPointerException("Something wrong happened when iterating through the config!");

                elementSetting = new ArmorGUIElementSetting(i, material, count, name, lore, model, new ArmorSetting(equipment));
            } else {
                elementSetting = new GUIElementSetting(i, material, count, name, lore, model);
            }

            elementSettings.put(i, elementSetting);
        }
    }
}