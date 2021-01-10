package io.github.mg138.tsbook.items;

import io.github.mg138.tsbook.items.data.Stat;
import io.github.mg138.tsbook.items.data.StatRange;
import io.github.mg138.tsbook.items.data.StatType;
import io.github.mg138.tsbook.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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
    private final HashMap<StatType, Stat> STAT = new HashMap<>();
    private Integer MODEL = null;

    public ItemInstance(ConfigAbstract config, YamlConfiguration setting, ItemIdentification identification) {
        CONFIG = setting;
        NAME = config.getLang().translate("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("stat")) {
            ConfigurationSection stats = CONFIG.getConfigurationSection("stat");
            if (stats == null) throw new IllegalArgumentException("I cannot properly get Damage of the item");

            for (String statType : stats.getKeys(false)) {
                if (stats.contains(statType + ".min")) {
                    StatType type = StatType.valueOf(statType.toUpperCase());
                    STAT.put(type, new Stat(
                            new StatRange(
                                    stats.getDouble(statType + ".max"),
                                    stats.getDouble(statType + ".min")
                            )
                    ));
                } else {
                    StatType type = StatType.valueOf(statType.toUpperCase());
                    STAT.put(type, new Stat(
                            stats.getDouble(statType)
                    ));
                }
            }
        }

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
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.contains("[damage-physical]")) {
                iterator.set(s.replace("[damage-physical]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_PHYSICAL,
                        config,
                        STAT.get(StatType.DAMAGE_PHYSICAL).statRange
                        ))
                );
            } else if (s.contains("[damage-terra]")) {
                iterator.set(s.replace("[damage-terra]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_TERRA,
                        config,
                        STAT.get(StatType.DAMAGE_TERRA).statRange
                        ))
                );
            } else if (s.contains("[damage-aer]")) {
                iterator.set(s.replace("[damage-aer]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_AER,
                        config,
                        STAT.get(StatType.DAMAGE_AER).statRange
                        ))
                );
            } else if (s.contains("[damage-ignis]")) {
                iterator.set(s.replace("[damage-ignis]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_IGNIS,
                        config,
                        STAT.get(StatType.DAMAGE_IGNIS).statRange
                        ))
                );
            } else if (s.contains("[damage-aqua]")) {
                iterator.set(s.replace("[damage-aqua]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_AQUA,
                        config,
                        STAT.get(StatType.DAMAGE_AQUA).statRange
                        ))
                );
            } else if (s.contains("[damage-lumen]")) {
                iterator.set(s.replace("[damage-lumen]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_LUMEN,
                        config,
                        STAT.get(StatType.DAMAGE_LUMEN).statRange
                        ))
                );
            } else if (s.contains("[damage-umbra]")) {
                iterator.set(s.replace("[damage-umbra]", IDENTIFICATION.getDesc(
                        StatType.DAMAGE_UMBRA,
                        config,
                        STAT.get(StatType.DAMAGE_UMBRA).statRange
                        ))
                );
            } else if (s.contains("[critical]")) {
                iterator.set(s.replace("[critical]", IDENTIFICATION.getDesc(
                        StatType.CRITICAL,
                        config,
                        STAT.get(StatType.CRITICAL)
                        ))
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

    public StatRange getStatRange(StatType type) {
        return STAT.get(type).statRange;
    }

    public ItemStack createItem(JavaPlugin plugin, String path) {
        return ItemUtils.createItem(plugin, this, path);
    }
}