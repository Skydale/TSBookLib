package io.github.twilight_book.items;

import com.google.common.math.StatsAccumulator;
import io.github.twilight_book.utils.config.ConfigAbstract;
import io.github.twilight_book.utils.papi.item;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemIdentification implements Serializable {
    private final HashMap<ItemUtils.StatsType,Integer> identificationValue = new HashMap<>();
    private final Random random;

    public ItemIdentification(ItemInstance i, Random random){
        this(i.getConfig(),random);
    }

    public ItemIdentification(ConfigAbstract config, String ID, Random random){
        this(config.getItemByID(ID),random);
    }

    public ItemIdentification(YamlConfiguration item, Random random){
        this.random = random;
        rollStats(item);
    }

    @Nonnull
    public Map<ItemUtils.StatsType,Integer> rollStats(YamlConfiguration item){
        for(String damageType : item.getConfigurationSection("stat.damage").getKeys(false)){
            Double statsPercentage = random.nextGaussian();
            ItemUtils.StatsType a = ItemUtils.StatsType.valueOf(damageType.toUpperCase());
            if(a==null) continue; //Invalid damage type
            ConfigurationSection damages = item.getConfigurationSection("stat.damage");
            Integer statsMin = damages.getConfigurationSection(damageType).getInt("min");
            Integer statsMax = damages.getConfigurationSection(damageType).getInt("max");
            Integer stats = (int) (((statsMax - statsMin)/2)*(statsPercentage+3)/3 + statsMin);
            //Bukkit.broadcastMessage(stats+"a");
            if(stats>statsMax) stats = statsMax;
            if(stats<statsMin) stats = statsMin;
            //Bukkit.broadcastMessage(stats+"b");
            identificationValue.put(a,stats);
        }
        return identificationValue;
    }

    @Nonnull
    public Map<ItemUtils.StatsType,Integer> getIdentifications(){
        return identificationValue;
    }

    @Nullable
    public Integer getStats(@Nonnull ItemUtils.StatsType stats){
        return identificationValue.get(stats);
    }

}
