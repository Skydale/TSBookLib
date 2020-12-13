package io.github.twilight_book.event;

import io.github.twilight_book.Book;
import io.github.twilight_book.items.ItemUtils;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamage implements Listener {
    BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();;

    @EventHandler
    public boolean onDamage(EntityDamageByEntityEvent event){
        if (helper.isMythicMob(event.getEntity())){
            return mythicMobsDamage(event);
        }
        return vanillaDamage(event);
    }

    public boolean vanillaDamage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof LivingEntity){
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (damager.getEquipment() == null) return true;

            YamlConfiguration item = Book.getCfg().getItemByID(
                ItemUtils.getDataTag(
                    Book.getCfg().getPlugin(), damager.getEquipment().getItemInMainHand(), "item"
                )
            );

            item.getMapList("damage").forEach(value -> System.out.println(value));
        } else {
            event.setDamage(0.0);
            return true;
        }
        return false;
    }

    public boolean mythicMobsDamage(EntityDamageByEntityEvent event){
        ActiveMob mythicmob = helper.getMythicMobInstance(event.getEntity());
        ConfigurationSection mob = Book.getCfg().getMMMob(mythicmob.getType().getInternalName());
        if (mob == null) return vanillaDamage(event);

        return false;
    }
}
