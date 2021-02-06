package io.github.mg138.tsbook.listener.event.damage.utils;

import io.github.mg138.tsbook.items.data.stat.StatType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomDamageEvent extends Event {
    private LivingEntity entity;
    private LivingEntity damager;
    private HashMap<StatType, Double> damages = new HashMap<>();
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public CustomDamageEvent(LivingEntity entity, LivingEntity damager){
        this.entity = entity;
        this.damager = damager;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public HashMap<StatType, Double> getDamages() {
        return damages;
    }

    public void setDamages(HashMap<StatType, Double> damages) {
        this.damages = damages;
    }

    public void addDamage(StatType type, Double damage) {
        this.damages.put(type, damage);
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public void setDamager(LivingEntity damager) {
        this.damager = damager;
    }
}