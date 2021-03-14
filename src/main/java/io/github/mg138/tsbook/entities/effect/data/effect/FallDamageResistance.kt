package io.github.mg138.tsbook.entities.effect.data.effect;

import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class FallDamageResistance {
    public static final Consumer<EntityStatusEffect> fallDamageResistance = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                cancel();
            }

            @Override
            public void cancel() {
                super.cancel();
                EffectHandler.remove(target, statusEffect.type);
            }
        };

        EffectHandler.applyRawEffect(target, statusEffect, runnable, statusEffect.ticks, 0);
    };
}
