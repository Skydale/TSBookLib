package io.github.mg138.tsbook.entities.effect.data.effect;

import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Levitation {
    public static final Consumer<EntityStatusEffect> levitation = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;

        target.removePotionEffect(PotionEffectType.LEVITATION);
        target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2147483647, (int) statusEffect.power));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                cancel();
            }

            @Override
            public void cancel() {
                super.cancel();
                target.removePotionEffect(PotionEffectType.LEVITATION);
                EffectHandler.remove(target, statusEffect.type);
            }
        };

        EffectHandler.applyRawEffect(target, statusEffect, runnable, statusEffect.ticks, 0);
    };
}
