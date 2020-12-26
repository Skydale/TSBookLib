package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemInstance { //represents a single ItemStack
    private final List<String> LORE;
    private final String NAME;
    private final Material MATERIAL;
    private final ItemIdentification IDENTIFICATION;
    private final YamlConfiguration CONFIG;
    private Integer MODEL = null;

    public ItemInstance(ConfigAbstract config, YamlConfiguration setting, ItemIdentification identification) {
        CONFIG = setting;
        NAME = config.getLang().translate("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("MODEL")) {
            MODEL = CONFIG.getInt("MODEL");
        }

        IDENTIFICATION = identification;
        updateLore(config);
    }

    public String getName() {
        return NAME;
    }

    public List<String> getLore() {
        return LORE;
    }

    public void updateLore(ConfigAbstract config) {
        ListIterator<String> iterator = LORE.listIterator();
        while(iterator.hasNext()) {
            String s = iterator.next();
            if (s.contains("[item.damage-physical]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-physical]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.PHYSICAL,
                                        config,
                                        CONFIG.getDouble("stat.damage.physical.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-terra]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-terra]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.TERRA,
                                        config,
                                        CONFIG.getDouble("stat.damage.terra.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-aer]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-aer]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.AER,
                                        config,
                                        CONFIG.getDouble("stat.damage.aer.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-ignis]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-ignis]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.IGNIS,
                                        config,
                                        CONFIG.getDouble("stat.damage.ignis.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-aqua]")) {
                iterator.set(
                        s.replaceAll("\\[item.damage-aqua]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.AQUA,
                                        config,
                                        CONFIG.getDouble("stat.damage.aqua.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-lumen]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-lumen]",
                                IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.LUMEN,
                                        config,
                                        CONFIG.getDouble("stat.damage.lumen.max")
                                )
                        )
                );
            }

            if (s.contains("[item.damage-umbra]")) {
                iterator.set(
                        s.replaceAll(
                                "\\[item.damage-umbra]", IDENTIFICATION.getDesc(
                                        ItemUtils.DamageType.UMBRA,
                                        config,
                                        CONFIG.getDouble("stat.damage.umbra.max")
                                )
                        )
                );
            }
        }
    }

    public YamlConfiguration getConfig() {
        return CONFIG;
    }

    @Nullable
    public ItemIdentification getIdentification() {
        return IDENTIFICATION;
    }

    public Material getMaterial() {
        return MATERIAL;
    }

    public Integer getModel() {
        return MODEL;
    }

    public ItemStack createItem(JavaPlugin plugin, String path) {
        return ItemUtils.createItem(plugin, this, path);
    }
}