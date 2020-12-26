package io.github.twilight_book.items;

import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ItemIdentification implements Serializable {
    private final HashMap<ItemUtils.DamageType, StatRange> stat = new HashMap<>();

    public ItemIdentification(ItemInstance inst) {
        this(inst.getConfig(), true);
    }

    public ItemIdentification(ConfigAbstract config, String ID) {
        this(config.getItemByID(ID), true);
    }

    public ItemIdentification(YamlConfiguration item, boolean isRandom) {
        ConfigurationSection stat = item.getConfigurationSection("stat");
        assert stat != null;
        for (String key : stat.getKeys(true)) {
            System.out.println(key);
        }
        //TODO SUPPORT OTHER STATS
        if (!item.contains("stat.damage")) return;
        ConfigurationSection damage = item.getConfigurationSection("stat.damage");
        if (damage == null) return;

        if (isRandom) {
            Random random = new Random();

            for (String damageType : damage.getKeys(false)) {
                double percentage = ((random.nextGaussian() / 4) + 1);

                if (percentage < 0) percentage = 0;
                else if (percentage > 1) percentage = 1;

                setIdentification(damageType, damage, percentage);
            }
        } else {
            for (String damageType : damage.getKeys(false)) {
                setIdentification(damageType, damage, 1);
            }
        }
    }

    private void setIdentification(String damageType, ConfigurationSection damage, double percentage) {
        ItemUtils.DamageType type = ItemUtils.DamageType.valueOf(damageType.toUpperCase());

        StatRange statRange = new StatRange(
                Objects.requireNonNull(damage.getConfigurationSection(damageType)).getDouble("min") * percentage,
                Objects.requireNonNull(damage.getConfigurationSection(damageType)).getDouble("max") * percentage
        );
        stat.put(type, statRange);
    }

    public HashMap<ItemUtils.DamageType, StatRange> getStat() {
        return stat;
    }

    public StatRange getStatRange(ItemUtils.@NonNull DamageType stat) {
        return this.stat.get(stat);
    }

    public String getDesc(ItemUtils.DamageType stat, ConfigAbstract config, double max) {
        String format = config.getLang().translate("format.damage-range." + stat.toString().toLowerCase());
        StatRange range = this.stat.get(stat);
        return format.replaceAll("\\[min]",        String.valueOf((int) range.min))
                     .replaceAll("\\[max]",        String.valueOf((int) range.max))
                     .replaceAll("\\[percentage]", String.valueOf((int) ((range.max / max) * 100)) + '%');
    }
}