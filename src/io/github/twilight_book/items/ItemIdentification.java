package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemIdentification {
    private final HashMap<ItemUtils.StatsType, Double> identification = new HashMap<>();
    private final Random random;
    private final YamlConfiguration item;

    public ItemIdentification(ItemInstance i, Random random) {
        this(i.getConfig(), random);
    }

    public ItemIdentification(ConfigAbstract config, String ID, Random random) {
        this(config.getItemByID(ID), random);
    }

    public ItemIdentification(YamlConfiguration item, Random random) {
        this.random = random;
        this.item = item;
        rollStats();
    }

    public void rollStats() {
        if (!item.contains("stat.damage")) return;
        for (String damageType : item.getConfigurationSection("stat.damage").getKeys(false)) {
            double statsPercentage = random.nextGaussian();
            ItemUtils.StatsType a = ItemUtils.StatsType.valueOf(damageType.toUpperCase());
            ConfigurationSection damages = item.getConfigurationSection("stat.damage");
            if (damages == null) continue;

            DamageRange damage = new DamageRange(
                    damages.getDouble(damageType + ".max"),
                    damages.getDouble(damageType + ".min")
            );
            double stats = (((damage.max - damage.min) / 2) * (statsPercentage + 3) / 3 + damage.min);
            Bukkit.broadcastMessage(stats + "a");
            if (stats > damage.max) stats = damage.max;
            if (stats < damage.min) stats = damage.min;
            Bukkit.broadcastMessage(stats + "b");
            identification.put(a, stats);
        }
    }

    @NotNull
    public Map<ItemUtils.StatsType, Double> getIdentifications() {
        return identification;
    }

    @Nullable
    public Double getStats(@NotNull ItemUtils.StatsType stats) {
        return identification.get(stats);
    }
}